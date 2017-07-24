package com.doormaster.topkeeper.bean;


 
/** 
* @ClassName: RoomBean 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (钟柳青)  
* @date 2016年10月15日 下午5:48:03 
* @version V1.0 
*/
public class RoomBean {
 
  /*
   * 房间所属小区编号，唯一键，用于区分小区
   */
  public String community_code;
  
  /*
   * 小区名称
   */
  public String community;
  
  /*
   * 楼栋单元名称
   */
  public String building;
  
  /*
   * 房间编号
   */
  public String room_code;

  /*
   * 房间名称
   */
  public String room_name;
/*
 * 房间id，和community_code一起确定唯一小区房间（删除或更新时id+community_code一起作为主键判断）
 */
  public int  room_id;
  
  /*
   * 房间有效开始时间，格式：年月日时分秒(yyyymmddHHMMSS)，空值则永久
   */
  public String start_datetime;
  
  /*
   * 房间结束时间，格式：年月日时分秒(yyyymmddHHMMSS),空值则永久
   */
  public String end_datetime;

    public String getCommunity_code() {
        return community_code;
    }

    public void setCommunity_code(String community_code) {
        this.community_code = community_code;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }


    @Override
	public String toString() {
		return "RoomBean [community_code=" + community_code + ", community="
				+ community + ", building=" + building + ", room_code=" + room_code
				+ ", room_name=" + room_name + ", room_id=" + room_id
				+ ", start_datetime=" + start_datetime + ", end_datetime="
				+ end_datetime + "]";
	}
  
}
