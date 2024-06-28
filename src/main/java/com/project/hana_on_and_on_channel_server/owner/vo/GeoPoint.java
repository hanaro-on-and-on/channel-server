package com.project.hana_on_and_on_channel_server.owner.vo;

import lombok.Getter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Getter
public class GeoPoint {
    private final double lng;
    private final double lat;

    public GeoPoint(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public GeoPoint(Point point) {
        this(point.getX(), point.getY());
    }

    public Point toPoint() {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        return factory.createPoint(new Coordinate(lng, lat));
    }
}
