/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.beans;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuditLogEventRequest {

  @NotBlank
  @Size(max = 36)
  private String correlationId;

  @NotNull private Boolean alert;

  @Size(max = 100)
  @ToString.Exclude
  private String userId;

  @NotBlank
  @Size(max = 30)
  private String systemId;

  @NotBlank
  @Size(max = 40)
  private String eventName;

  @NotBlank
  @Size(min = 7, max = 39)
  @ToString.Exclude
  private String systemIp;

  @NotBlank
  @Size(min = 7, max = 39)
  @ToString.Exclude
  private String clientIp;

  @NotBlank
  @Size(max = 255)
  @ToString.Exclude
  private String description;

  @NotBlank
  @Size(max = 255)
  @ToString.Exclude
  private String eventDetail;

  @NotBlank
  @Size(max = 60)
  private String applicationVersion;

  @NotBlank
  @Size(max = 100)
  private String applicationComponentName;

  /** the number of milliseconds from the epoch of 1970-01-01T00:00:00Z */
  @NotNull private Long occured;

  @Size(max = 100)
  private String appId;

  @Size(max = 100)
  private String clientId;

  @Size(max = 10)
  private String deviceType;

  @Size(max = 255)
  @ToString.Exclude
  private String requestUri;

  @Size(max = 10)
  private String accessLevel;

  @Size(max = 100)
  private String devicePlatform;

  @Size(max = 40)
  private String resourceServer;

  @Size(max = 20)
  private String clientAppVersion;

  @Size(max = 20)
  private String clientAccessLevel;
}
