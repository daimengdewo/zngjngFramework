package com.awen.energy.protocol.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceMessage implements Serializable {
    private byte[] deviceid;
}