package com.dotcms.plugin.hubspot.osgi;

import com.dotcms.plugin.hubspot.actionlet.ContactSave;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotmarketing.osgi.GenericBundleActivator;

/**
 * @author Jonathan Gamba
 *         7/29/16
 */
public class Activator extends GenericBundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        //Initializing services...
        initializeServices(bundleContext);

        //Registering the hubspot Actionlet
        registerActionlet(bundleContext, new ContactSave());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        unregisterActionlets();
    }

}