package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-05-16.
 */
public class Blocked_Cards_Info {
    int blocked_cards_id;
    String card_serial;
    String Server_timestamp;


    public Blocked_Cards_Info() {
    }

    public Blocked_Cards_Info(int blocked_cards_id, String card_serial, String server_timestamp) {
        this.blocked_cards_id = blocked_cards_id;
        this.card_serial = card_serial;
        Server_timestamp = server_timestamp;
    }


    public Blocked_Cards_Info(int blocked_cards_id, String card_serial) {
        this.blocked_cards_id = blocked_cards_id;
        this.card_serial = card_serial;
    }

    public int getBlocked_cards_id() {
        return blocked_cards_id;
    }

    public void setBlocked_cards_id(int blocked_cards_id) {
        this.blocked_cards_id = blocked_cards_id;
    }

    public String getCard_serial() {
        return card_serial;
    }

    public void setCard_serial(String card_serial) {
        this.card_serial = card_serial;
    }

    public String getServer_timestamp() {
        return Server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        Server_timestamp = server_timestamp;
    }
}
