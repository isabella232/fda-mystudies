/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Component
@Scope(value = "prototype")
public class AppStudyResponse {
  private String studyId;

  private String customStudyId;

  private String studyName;

  private Boolean selected = false;

  private Boolean disabled = false;

  private String permission;

  private String totalSitesCount;

  private String selectedSitesCount;

  private List<AppSiteResponse> sites = new ArrayList<>();
}
