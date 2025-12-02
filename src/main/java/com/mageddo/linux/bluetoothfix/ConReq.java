package com.mageddo.linux.bluetoothfix;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Value
@Builder
@Accessors(fluent = true)
public class ConReq {
  @NonNull
  String deviceId;

  String controllerId;

  public boolean hasControllerId() {
    return StringUtils.isNotBlank(this.controllerId);
  }

  public boolean hasNotControllerId() {
    return !this.hasControllerId();
  }
}
