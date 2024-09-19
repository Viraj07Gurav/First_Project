package com.example.timetablegenerator.dbModel;

public class RoomModel {
    String roomNo, seatingCap, rId;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getSeatingCap() {
        return seatingCap;
    }

    public void setSeatingCap(String seatingCap) {
        this.seatingCap = seatingCap;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }
}
