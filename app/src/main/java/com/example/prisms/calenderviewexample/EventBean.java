package com.example.prisms.calenderviewexample;

import java.io.Serializable;

public class EventBean implements Serializable {

	private String eventTitle;
	private String eventDescr;
	private String eventDate;
	private String eventColor;
	private String calenderTitle;
	private int eventId;
	private int calId;
	private int id;
	private String eventEndDate;
	
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventDescr() {
		return eventDescr;
	}
	public void setEventDescr(String eventDescr) {
		this.eventDescr = eventDescr;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public int getCalId() {
		return calId;
	}
	public void setCalId(int calId) {
		this.calId = calId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEventColor() {
		return eventColor;
	}
	public void setEventColor(String eventColor) {
		this.eventColor = eventColor;
	}
	public String getCalenderTitle() {
		return calenderTitle;
	}
	public void setCalenderTitle(String calenderTitle) {
		this.calenderTitle = calenderTitle;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	
}
