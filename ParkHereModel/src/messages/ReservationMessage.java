package messages;

import java.io.Serializable;

import model.Reservation;

public class ReservationMessage extends Message implements Serializable{
	public Reservation reservation;
}
