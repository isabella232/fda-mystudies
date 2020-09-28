/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.fdahpstudydesigner.controller;

import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.LAST_PUBLISHED_VERSION_OF_STUDY_VIEWED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.NEW_STUDY_CREATION_INITIATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_ACCESSED_IN_EDIT_MODE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_BASIC_INFO_SECTION_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_BASIC_INFO_SECTION_SAVED_OR_UPDATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_CONSENT_SECTIONS_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_DEACTIVATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_LAUNCHED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_NEW_NOTIFICATION_CREATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_NOTIFICATIONS_SECTION_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_NOTIFICATION_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_NOTIFICATION_SAVED_OR_UPDATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_PAUSED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_PUBLISHED_AS_UPCOMING_STUDY;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_QUESTIONNAIRES_SECTION_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_RESOURCE_MARKED_COMPLETED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_RESOURCE_SAVED_OR_UPDATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_RESOURCE_SECTION_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_RESUMED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_SAVED_IN_DRAFT_STATE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_SETTINGS_MARKED_COMPLETE;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_SETTINGS_SAVED_OR_UPDATED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.STUDY_VIEWED;
import static com.fdahpstudydesigner.common.StudyBuilderAuditEvent.UPDATES_PUBLISHED_TO_STUDY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fdahpstudydesigner.bean.StudySessionBean;
import com.fdahpstudydesigner.bo.NotificationBO;
import com.fdahpstudydesigner.bo.ResourceBO;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.common.BaseMockIT;
import com.fdahpstudydesigner.common.PathMappingUri;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class StudyControllerTest extends BaseMockIT {

  private static final String STUDY_ID_VALUE = "678574";

  private static final String CUSTOM_STUDY_ID_VALUE = "678590";

  private static final String USER_ID_VALUE = "4878641";

  @Test
  public void shouldMarkActiveTaskAsCompleted() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.CONSENT_MARKED_AS_COMPLETE.getPath())
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:comprehensionQuestionList.do"));

    verifyAuditEventCall(STUDY_CONSENT_SECTIONS_MARKED_COMPLETE);
  }

  @Test
  public void shouldMarkNotificationAsComplete() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.NOTIFICATION_MARK_AS_COMPLETED.getPath())
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:getChecklist.do"));

    verifyAuditEventCall(STUDY_NOTIFICATIONS_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void shouldMarkQuestionaireSectionAsComplete() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            get(PathMappingUri.QUESTIONAIRE_MARK_AS_COMPLETED.getPath())
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:viewStudyActiveTasks.do"));

    verifyAuditEventCall(STUDY_QUESTIONNAIRES_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void shouldMarkStudyResourceSectionAsComplete() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.RESOURCE_MARK_AS_COMPLETED.getPath())
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:viewStudyNotificationList.do"));

    verifyAuditEventCall(STUDY_RESOURCE_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void checkNewStudyCreationInitiated() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    StudySessionBean studySessionBean = new StudySessionBean();
    studySessionBean.setIsLive("live");
    studySessionBean.setPermission("permission");
    studySessionBean.setStudyId(STUDY_ID_VALUE);
    studySessionBean.setSessionStudyCount(0);

    List<StudySessionBean> studySessionBeans = new ArrayList<>();
    studySessionBeans.add(studySessionBean);
    SessionObject session = getSessionObject();
    session.setStudySessionBeans(studySessionBeans);
    session.setUserId(0);

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);

    mockMvc
        .perform(
            post(PathMappingUri.VIEW_STUDY_DETAILS.getPath())
                .param(FdahpStudyDesignerConstants.IS_LIVE, "live")
                .param(FdahpStudyDesignerConstants.PERMISSION, "permission")
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/adminStudies/viewBasicInfo.do"));

    verifyAuditEventCall(NEW_STUDY_CREATION_INITIATED);
  }

  @Test
  public void checkLastPublishedVersionOfStudiedViewed() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    mockMvc
        .perform(
            post(PathMappingUri.VIEW_STUDY_DETAILS.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.PERMISSION, "View")
                .param(FdahpStudyDesignerConstants.IS_LIVE, "isLive")
                .headers(headers)
                .sessionAttrs(getSessionAttributes()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/adminStudies/viewBasicInfo.do"));

    verifyAuditEventCall(LAST_PUBLISHED_VERSION_OF_STUDY_VIEWED);
  }

  @Test
  public void checkStudyViewed() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    mockMvc
        .perform(
            post(PathMappingUri.VIEW_STUDY_DETAILS.getPath())
                .param(FdahpStudyDesignerConstants.PERMISSION, "View")
                .headers(headers)
                .sessionAttrs(getSessionAttributes()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/adminStudies/viewBasicInfo.do"));

    verifyAuditEventCall(STUDY_VIEWED);
  }

  @Test
  public void checkStudyAccessedInEditMode() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    mockMvc
        .perform(
            post(PathMappingUri.VIEW_STUDY_DETAILS.getPath())
                .headers(headers)
                .sessionAttrs(getSessionAttributes()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/adminStudies/viewBasicInfo.do"));

    verifyAuditEventCall(STUDY_ACCESSED_IN_EDIT_MODE);
  }

  @Test
  public void shouldLaunchStudy() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = getSessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.UPDATE_STUDY_ACTION.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "lunchId")
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isOk());

    verifyAuditEventCall(STUDY_LAUNCHED);
  }

  @Test
  public void shouldUpdateStudyAction() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.UPDATE_STUDY_ACTION.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "publishId")
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isOk());

    verifyAuditEventCall(STUDY_PUBLISHED_AS_UPCOMING_STUDY);
  }

  @Test
  public void shouldUpdatesPublishedToStudy() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.UPDATE_STUDY_ACTION.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "updatesId")
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isOk());

    verifyAuditEventCall(UPDATES_PUBLISHED_TO_STUDY);
  }

  @Test
  public void shouldPauseStudy() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.UPDATE_STUDY_ACTION.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "pauseId")
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isOk());

    verifyAuditEventCall(STUDY_PAUSED);
  }

  @Test
  public void shouldResumeStudy() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.UPDATE_STUDY_ACTION.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "resumeId")
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isOk());

    verifyAuditEventCall(STUDY_RESUMED);
  }

  @Test
  public void shouldCompleteSyudySettings() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(STUDY_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);

    StudyBo studyBo = new StudyBo();
    studyBo.setId(678574);
    studyBo.setStudySequenceBo(null);

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_SETTINGS_AND_ADMINS.getPath())
            .param("userIds", STUDY_ID_VALUE)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "completed")
            .headers(headers)
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, studyBo);
    mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isFound());

    verifyAuditEventCall(STUDY_SETTINGS_MARKED_COMPLETE);
  }

  @Test
  public void shouldSaveOrUpdateSyudySettings() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(STUDY_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);

    StudyBo studyBo = new StudyBo();
    studyBo.setId(678574);
    studyBo.setStudySequenceBo(null);

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_SETTINGS_AND_ADMINS.getPath())
            .param("userIds", STUDY_ID_VALUE)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "save")
            .headers(headers)
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, studyBo);
    mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isFound());

    verifyAuditEventCall(STUDY_SETTINGS_SAVED_OR_UPDATED);
  }

  @Test
  public void shouldDeactivateStudy() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.UPDATE_STUDY_ACTION.getPath())
                .param(FdahpStudyDesignerConstants.STUDY_ID, STUDY_ID_VALUE)
                .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "deactivateId")
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isOk());

    verifyAuditEventCall(STUDY_DEACTIVATED);
    // verifyAuditEventCall(STUDY_METADATA_SENT_TO_PARTICIPANT_DATASTORE,STUDY_METADATA_SEND_OPERATION_FAILED,
    // STUDY_METADATA_SENT_TO_RESPONSE_DATASTORE,STUDY_METADATA_SEND_FAILED);
  }

  @Test
  public void shouldSaveStudyInDraftState() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    StudyBo studyBo = new StudyBo();
    studyBo.setId(678574);
    studyBo.setCustomStudyId(CUSTOM_STUDY_ID_VALUE);
    studyBo.setStudySequenceBo(null);

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_BASIC_INFO.getPath())
            .headers(headers)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "save")
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, studyBo);

    mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isFound());

    verifyAuditEventCall(STUDY_SAVED_IN_DRAFT_STATE);
  }

  @Test
  public void shouldMarkStudySectionAsComplete() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    StudyBo studyBo = new StudyBo();
    studyBo.setId(678574);
    studyBo.setCustomStudyId(CUSTOM_STUDY_ID_VALUE);
    studyBo.setStudySequenceBo(null);

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_BASIC_INFO.getPath())
            .headers(headers)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "completed")
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, studyBo);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:viewSettingAndAdmins.do"));

    verifyAuditEventCall(STUDY_BASIC_INFO_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void shouldSaveOrUpdateStudySection() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    StudyBo studyBo = new StudyBo();
    studyBo.setId(678574);
    studyBo.setCustomStudyId(CUSTOM_STUDY_ID_VALUE);
    studyBo.setStudySequenceBo(null);

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_BASIC_INFO.getPath())
            .headers(headers)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "save")
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, studyBo);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:viewBasicInfo.do"));

    verifyAuditEventCall(STUDY_BASIC_INFO_SECTION_SAVED_OR_UPDATED);
  }

  @Test
  public void shouldSaveOrUpdateStudyResource() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    ResourceBO ResourceBO = new ResourceBO();

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_RESOURCE.getPath())
            .headers(headers)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "save")
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, ResourceBO);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:addOrEditResource.do"));

    verifyAuditEventCall(STUDY_RESOURCE_SAVED_OR_UPDATED);
  }

  @Test
  public void shouldMarkStudyResourceAsComplete() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(STUDY_ID_ATTR_NAME, STUDY_ID_VALUE);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    ResourceBO ResourceBO = new ResourceBO();

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_RESOURCE.getPath())
            .headers(headers)
            .param(FdahpStudyDesignerConstants.BUTTON_TEXT, "completed")
            .sessionAttrs(sessionAttributes);

    addParams(requestBuilder, ResourceBO);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:getResourceList.do"));

    verifyAuditEventCall(STUDY_RESOURCE_MARKED_COMPLETED);
  }

  @Test
  public void shouldSaveOrUpdateStudyEligibilitySection() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    mockMvc
        .perform(
            get(PathMappingUri.SAVE_OR_UPDATE_STUDY_ELIGIBILITY.getPath())
                .headers(headers)
                .sessionAttrs(getSessionAttributes()))
        .andDo(print())
        .andExpect(status().isFound());

    // verifyAuditEventCall(STUDY_ELIGIBILITY_SECTION_SAVED_OR_UPDATED);
  }

  @Test
  public void shouldMarkStudyEligibilitySectionAsComplete() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    mockMvc
        .perform(
            get(PathMappingUri.SAVE_OR_UPDATE_STUDY_ELIGIBILITY.getPath())
                .headers(headers)
                .sessionAttrs(getSessionAttributes()))
        .andDo(print())
        .andExpect(status().isFound());

    // verifyAuditEventCall(STUDY_ELIGIBILITY_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void checkChecklistSavedOrCompleted() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    mockMvc
        .perform(
            get(PathMappingUri.SAVE_OR_DONE_CHECKLIST.getPath())
                .headers(headers)
                .sessionAttrs(getSessionAttributes()))
        .andDo(print())
        .andExpect(status().isFound());
    // TODO: check in audit log sheet, missing events
    // verifyAuditEventCall(STUDY_CHECKLIST_SECTION_SAVED_OR_UPDATED,STUDY_CHECKLIST_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void shouldSaveEConsent() throws Exception {
    HttpHeaders headers = getCommonHeaders();
    SessionObject session = new SessionObject();
    session.setUserId(Integer.parseInt(USER_ID_VALUE));
    session.setStudySession(new ArrayList<>(Arrays.asList(0)));
    session.setSessionId(UUID.randomUUID().toString());

    HashMap<String, Object> sessionAttributes = getSessionAttributes();
    sessionAttributes.put(FdahpStudyDesignerConstants.SESSION_OBJECT, session);
    sessionAttributes.put(CUSTOM_STUDY_ID_ATTR_NAME, CUSTOM_STUDY_ID_VALUE);

    mockMvc
        .perform(
            post(PathMappingUri.SAVE_CONSENT_REVIEW_AND_ECONSENT_INFO.getPath())
                .param("consentInfo", STUDY_ID_VALUE)
                .headers(headers)
                .sessionAttrs(sessionAttributes))
        .andDo(print())
        .andExpect(status().isFound());
    // verifyAuditEventCall(STUDY_REVIEW_AND_E_CONSENT_SAVED_OR_UPDATED);
    // verifyAuditEventCall(STUDY_REVIEW_AND_E_CONSENT_MARKED_COMPLETE, <-- modified Place holder
    // STUDY_COMPREHENSION_TEST_SECTION_SAVED_OR_UPDATED,
    // STUDY_COMPREHENSION_TEST_SECTION_MARKED_COMPLETE);
  }

  @Test
  public void shouldSaveOrUpdateOrResendNotificationForSave() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    NotificationBO notificationBo = new NotificationBO();
    notificationBo.setNotificationText("Study notification");

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_STUDY_NOTIFICATION.getPath())
            .headers(headers)
            .param("buttonType", "save")
            .sessionAttrs(getSessionAttributes());

    addParams(requestBuilder, notificationBo);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:getStudyNotification.do"));

    verifyAuditEventCall(STUDY_NOTIFICATION_SAVED_OR_UPDATED);
  }

  @Test
  public void shouldSaveOrUpdateOrResendNotificationForAdd() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    NotificationBO notificationBo = new NotificationBO();
    notificationBo.setNotificationText("Study notification");

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_STUDY_NOTIFICATION.getPath())
            .headers(headers)
            .param("buttonType", "add")
            .sessionAttr("copyAppNotification", true)
            .sessionAttrs(getSessionAttributes());

    addParams(requestBuilder, notificationBo);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/adminStudies/viewStudyNotificationList.do"));

    verifyAuditEventCall(STUDY_NEW_NOTIFICATION_CREATED);
  }

  @Test
  public void shouldSaveOrUpdateOrResendNotificationForDone() throws Exception {
    HttpHeaders headers = getCommonHeaders();

    NotificationBO notificationBo = new NotificationBO();
    notificationBo.setNotificationText("Study notification");

    MockHttpServletRequestBuilder requestBuilder =
        post(PathMappingUri.SAVE_OR_UPDATE_STUDY_NOTIFICATION.getPath())
            .headers(headers)
            .param("buttonType", "done")
            .sessionAttrs(getSessionAttributes());

    addParams(requestBuilder, notificationBo);

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/adminStudies/viewStudyNotificationList.do"));

    verifyAuditEventCall(STUDY_NOTIFICATION_MARKED_COMPLETE);
  }
}
