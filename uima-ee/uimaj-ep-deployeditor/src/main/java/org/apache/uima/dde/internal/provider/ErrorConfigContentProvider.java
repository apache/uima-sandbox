package org.apache.uima.dde.internal.provider;

import org.apache.uima.aae.deployment.AEDeploymentDescription;
import org.apache.uima.aae.deployment.AEDeploymentMetaData;
import org.apache.uima.aae.deployment.AsyncAEErrorConfiguration;
import org.apache.uima.aae.deployment.AsyncAggregateErrorConfiguration;
import org.apache.uima.aae.deployment.AsyncPrimitiveErrorConfiguration;
import org.apache.uima.aae.deployment.CollectionProcessCompleteErrors;
import org.apache.uima.aae.deployment.GetMetadataErrors;
import org.apache.uima.aae.deployment.ProcessCasErrors;
import org.apache.uima.aae.deployment.impl.DeploymentMetaData_Impl;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class ErrorConfigContentProvider implements IStructuredContentProvider {
  private static final Object[] EMPTY_ARRAY = new Object[0];

  public Object[] getElements(Object inputElement) {
    Object[] objs = null;
    int count = 0;
    int status = 0;

    if (inputElement instanceof GetMetadataErrors) {
      GetMetadataErrors getMetadataErrors = (GetMetadataErrors) inputElement;
      DeploymentMetaData_Impl parentMetaData = getMetadataErrors.getParent().gParentObject();
      
      if ( (parentMetaData instanceof AEDeploymentMetaData) 
              && !((AEDeploymentMetaData) parentMetaData).isAsync() ) {
        status = NameValuePair.STATUS_NON_EDITABLE;
      }
      
      objs = new Object[3];
      objs[0] = new NameValuePair(getMetadataErrors, GetMetadataErrors.KIND_MAX_RETRIES,
              "Max Retries", getMetadataErrors.getMaxRetries(), Integer.class);
      objs[1] = new NameValuePair(getMetadataErrors, GetMetadataErrors.KIND_TIMEOUT, "Timeout (in msec)",
              getMetadataErrors.getTimeout(), Integer.class);
      objs[2] = new NameValuePair(getMetadataErrors, GetMetadataErrors.KIND_ERRORACTION,
              "Error Action", getMetadataErrors.getErrorAction(), String.class);
      ((NameValuePair) objs[2]).setStatusFlags(status);
      return objs;

    } else if (inputElement instanceof ProcessCasErrors) {
      ProcessCasErrors processCasErrors = (ProcessCasErrors) inputElement;
      AsyncAEErrorConfiguration parent = processCasErrors.getParent();
      if (parent instanceof AsyncAggregateErrorConfiguration) {
        count = 6;
      } else {
        count = 3;
      }
      objs = new Object[count];
      
      if (count == 6) {
        objs[0] = new NameValuePair(processCasErrors, ProcessCasErrors.KIND_MAX_RETRIES,
                "Max Retries", processCasErrors.getMaxRetries(), Integer.class);
        objs[1] = new NameValuePair(processCasErrors, ProcessCasErrors.KIND_TIMEOUT, "Timeout (in msec)",
                processCasErrors.getTimeout(), Integer.class);
        objs[2] = new NameValuePair(processCasErrors, ProcessCasErrors.KIND_CONTINUE_ON_RETRY,
                "Continue On Retry Failure", processCasErrors.isContinueOnRetryFailure(),
                Boolean.class);
      }
      objs[count-3] = new NameValuePair(processCasErrors, ProcessCasErrors.KIND_THRESHOLD_COUNT,
              "Threshold Count", processCasErrors.getThresholdCount(), Integer.class);
      objs[count-2] = new NameValuePair(processCasErrors, ProcessCasErrors.KIND_THRESHOLD_WINDOW,
              "Threshold Window", processCasErrors.getThresholdWindow(), Integer.class);
      objs[count-1] = new NameValuePair(processCasErrors, ProcessCasErrors.KIND_THRESHOLD_ACTION,
              "Threshold Action", processCasErrors.getThresholdAction(), String.class);
      if (processCasErrors.getThresholdCount() == 0) {
        status = NameValuePair.STATUS_NON_EDITABLE;
        ((NameValuePair) objs[count-2]).setStatusFlags(NameValuePair.STATUS_NON_EDITABLE);
        ((NameValuePair) objs[count-1]).setStatusFlags(NameValuePair.STATUS_NON_EDITABLE);
      } else {
        // For Async Primitive Error
        if (processCasErrors.getParent() instanceof AsyncPrimitiveErrorConfiguration) {
          ((NameValuePair) objs[count-1]).setStatusFlags(NameValuePair.STATUS_NON_EDITABLE);
        }
      }
      return objs;

    } else if (inputElement instanceof CollectionProcessCompleteErrors) {
      CollectionProcessCompleteErrors completeErrors = (CollectionProcessCompleteErrors) inputElement;

      objs = new Object[2];
      objs[0] = new NameValuePair(completeErrors, CollectionProcessCompleteErrors.KIND_TIMEOUT,
              "Timeout (in msec)", completeErrors.getTimeout(), Integer.class);
      objs[1] = new NameValuePair(completeErrors,
              CollectionProcessCompleteErrors.KIND_ADDITIONA_ERROR_ACTION,
              "Additional Error Action", completeErrors.getAdditionalErrorAction(), String.class);
      if (completeErrors.getParent() instanceof AsyncPrimitiveErrorConfiguration) {
        ((NameValuePair) objs[1]).setStatusFlags(NameValuePair.STATUS_NON_EDITABLE);
      }
      return objs;

    }

    return EMPTY_ARRAY;
  }

  public void dispose() {
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

}
