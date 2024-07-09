#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
FRONT_PROJECT_NAME=/front
JAR_NAME=$REPOSITORY/hana_on_and_on_channel_server-0.0.1-SNAPSHOT.jar

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(lsof -ti :8080 | xargs)
#CURRENT_PID=$(pgrep -f hana_on_and_on_channel_server-0.0.1-SNAPSHOT.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다"
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"


echo "> JAR_NAME: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 소유권 변경"
sudo chown ec2-user $JAR_NAME

echo "> nohup.out 에 실행권한 추가"
chmod +x $REPOSITORY/nohup.out

echo "> nohup.out 소유권 변경"
sudo chown ec2-user $REPOSITORY/nohup.out

echo "> $JAR_NAME 실행"
nohup java -jar -Dspring.config.location=file:///home/ec2-user/app/step2/application.yml $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
# java -jar hana_on_and_on_channel_server-0.0.1-SNAPSHOT.jar > /home/ec2-user/app/step2/nohup.out 2>&1 &


# 프론트 배포
echo "> run client project"
cd $REPOSITORY/$FRONT_PROJECT_NAME

echo "> pm2 kill"
pm2 kill

echo "> git pull"
git pull

echo "> yarn dev"
yarn dev

echo "> pm2 build"
pm2 serve build/ 3000 --spa