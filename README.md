# plugin-hubspot-contact-save

This Actionlet allows to post content from a Content entry to a Hubspot Contact form using the Hubspot API.

## How to install the plugin:
 * Generate the OSGI plugin jar file
 ```
 ./gradlew jar
 ```
 The command above will generate the plugin jar file under *build/libs/plugin-hubspot-contact-save-0.1.jar*
 * Install the OSGI plugin: http://dotcms.com/docs/latest/osgi-plugins
 * Add the created Actionlet by the OSGI (**Hubspot Contact Form**) to a Workflow (The configuration parameters for the
 created Actionlet are after these steps)
 * Create the Content Type we will use to create and send content to Hubspot
 * Add to that new created Content Type the Workflow that contains the **"Hubspot Contact Form"**

## Actionlet parameters:

### Hubspot Portal Id
How to get the Portal Id: https://knowledge.hubspot.com/articles/kcs_article/account/where-can-i-find-my-hubspot-portal-id

### Hubspot Form Id
How to get the Form Id: https://knowledge.hubspot.com/articles/kcs_article/forms/how-do-i-find-the-form-guid

### Hubspot Fields Mapping:
On Hubspot each field of the contact form have an id, in order to map each of the fields of the *Content Type* using this Actionlet with those Hubspot fields we use the **Hubspot Fields Mapping** parameter, with that parameter we map the dotCMS field varname with the hotspot Contact Form field id.

* **Example:**
dotCMSFieldVarname1:hotspotFieldId1, dotCMSFieldVarname2:hotspotFieldId2, dotCMSFieldVarname3:hotspotFieldId3

**NOTE:** Only mapped fields are going to be sent to the Hubspot API

## Screen shoots
![actionlet](https://cloud.githubusercontent.com/assets/923947/17302614/3d0dddfe-57da-11e6-9715-16ffdf9d0fa9.png)
![contenttype](https://cloud.githubusercontent.com/assets/923947/17302611/3c6dff78-57da-11e6-9df4-9bd523c5e07f.png)
![contentlet](https://cloud.githubusercontent.com/assets/923947/17302612/3c6fade6-57da-11e6-8071-2bd5fd22212a.png)
