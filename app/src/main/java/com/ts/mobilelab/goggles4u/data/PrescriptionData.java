package com.ts.mobilelab.goggles4u.data;

/**
 * Created by PC2 on 15-04-2016.
 */
public class PrescriptionData {


    String prescription_id;

    public String getPrescriptiion_name() {
        return prescriptiion_name;
    }

    public void setPrescriptiion_name(String prescriptiion_name) {
        this.prescriptiion_name = prescriptiion_name;
    }

    public String getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(String prescription_id) {
        this.prescription_id = prescription_id;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    String prescriptiion_name;
    String createdby;
    String modifiedby;
    String createdate;



}
