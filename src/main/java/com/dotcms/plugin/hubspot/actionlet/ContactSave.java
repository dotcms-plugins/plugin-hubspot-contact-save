package com.dotcms.plugin.hubspot.actionlet;

import com.dotcms.repackage.org.apache.commons.httpclient.HttpClient;
import com.dotcms.repackage.org.apache.commons.httpclient.HttpStatus;
import com.dotcms.repackage.org.apache.commons.httpclient.methods.PostMethod;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.workflows.actionlet.WorkFlowActionlet;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClassParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowActionFailureException;
import com.dotmarketing.portlets.workflows.model.WorkflowActionletParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowProcessor;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jonathan Gamba
 *         7/29/16
 */
public class ContactSave extends WorkFlowActionlet {

    private static final long serialVersionUID = 1L;

    //https://developers.hubspot.com/docs/methods/forms/submit_form
    //https://forms.hubspot.com/uploads/form/v2/:portal_id/:form_guid
    private final String POST_URL = "https://forms.hubspot.com/uploads/form/v2";
    private final String PORTAL_ID = "2389934";
    private final String HUBSPOT_FORM_ID = "11348411-6dde-4f26-9be5-f365cdda7a85";

    private final String PARAMETER_FIELDS_MAPPING = "fieldsMapping";
    private final String PARAMETER_PORTAL_ID = "portalId";
    private final String PARAMETER_FORM_ID = "formId";

    private final String SPLIT_STRING = ",";
    private final String SPLIT_MAPPING_STRING = ":";

    @Override
    public List<WorkflowActionletParameter> getParameters() {
        List<WorkflowActionletParameter> params = new ArrayList<>();
        params.add(new WorkflowActionletParameter(PARAMETER_FIELDS_MAPPING, "Hubspot Fields Mapping", "", true));
        params.add(new WorkflowActionletParameter(PARAMETER_PORTAL_ID, "HubSpot Portal Id", PORTAL_ID, true));
        params.add(new WorkflowActionletParameter(PARAMETER_FORM_ID, "HubSpot Form Id", HUBSPOT_FORM_ID, true));
        return params;
    }

    @Override
    public String getName() {
        return "Hubspot Contact Form";
    }

    @Override
    public String getHowTo() {
        return "This actionlet will post content from a Content entry to a Hubspot Contact form.";
    }

    @Override
    public void executeAction(WorkflowProcessor processor, Map<String, WorkflowActionClassParameter> parameters) throws WorkflowActionFailureException {

        //Get the content we want to process
        Contentlet contentletToProcess = processor.getContentlet();

        Map<String, String> parametersMap = new HashMap<>();

        try {
            //TODO: getParameters() VS parameters ???
            //TODO: getParameters() VS parameters ???
            //TODO: getParameters() VS parameters ???

            //TODO: Can I just parameters.get(PARAMETER_PORTAL_ID).getValue() --> This handle the default value???

            //Read the Actionlet parameters
            for ( WorkflowActionletParameter param : getParameters() ) {
                String paramKey = param.getKey();
                String paramVal = parameters.get(paramKey).getValue();

                if ( !UtilMethods.isSet(paramVal) ) {
                    paramVal = param.getDefaultValue();
                }

                parametersMap.put(paramKey, paramVal);
            }
        } catch (Exception e) {
            Logger.error(this.getClass(), e.getMessage(), e);
            throw new WorkflowActionFailureException(e.getMessage(), e);
        }

        //Create the client and post method in order to submit the content to the hubspot API
        HttpClient client = new HttpClient();
        /*
         https://developers.hubspot.com/docs/methods/forms/submit_form
         https://forms.hubspot.com/uploads/form/v2/:portal_id/:form_guid
         */
        PostMethod postMethod = new PostMethod(POST_URL +
                "/" + parametersMap.get(PARAMETER_PORTAL_ID) +
                "/" + parametersMap.get(PARAMETER_FORM_ID));

        //Prepare the post parameters using the mapping between the contentlet and the Hubspot fields
        String fieldsMapping = parametersMap.get(PARAMETER_FIELDS_MAPPING);//dotCMSField1:prop1, dotCMSField2:prop2
        String[] fieldsMappingArray = fieldsMapping.split(SPLIT_STRING);
        for ( String mapping : fieldsMappingArray ) {
            String fields[] = mapping.split(SPLIT_MAPPING_STRING);
            String dotCMSVarName = fields[0];
            String hubspotProperty = fields[1];

            //Getting the dotCMS field value
            String fieldValue = contentletToProcess.getStringProperty(dotCMSVarName);
            if ( UtilMethods.isSet(fieldValue) ) {
                //Add the form parameter for the post call
                postMethod.addParameter(hubspotProperty, fieldValue);
            }
        }

        try {
            //And finally submit the contact info
            client.executeMethod(postMethod);

            int responseStatusCode = postMethod.getStatusCode();
            if ( responseStatusCode == HttpStatus.SC_OK || responseStatusCode == HttpStatus.SC_NO_CONTENT ) {//Everything ok...
                Logger.info(this.getClass(), "Successfully submit to Hubspot Contact Form");
            }
        } catch (IOException e) {
            Logger.error(this.getClass(), e.getMessage(), e);
            throw new WorkflowActionFailureException(e.getMessage(), e);
        } finally {
            postMethod.releaseConnection();
        }
    }

}