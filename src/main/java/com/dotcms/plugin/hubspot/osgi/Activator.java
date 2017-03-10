package com.dotcms.plugin.hubspot.osgi;

import com.dotcms.plugin.hubspot.actionlet.ContactSave;
import com.dotcms.repackage.org.apache.logging.log4j.LogManager;
import com.dotcms.repackage.org.apache.logging.log4j.core.LoggerContext;
import org.osgi.framework.BundleContext;
import com.dotmarketing.loggers.Log4jUtil;
import com.dotmarketing.osgi.GenericBundleActivator;

/**
 * @author Jonathan Gamba
 *         7/29/16
 */
public class Activator extends GenericBundleActivator {

    private LoggerContext pluginLoggerContext;

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        //Initializing log4j...
        LoggerContext dotcmsLoggerContext = Log4jUtil.getLoggerContext();
        //Initialing the log4j context of this plugin based on the dotCMS logger context
        pluginLoggerContext = (LoggerContext) LogManager.getContext(this.getClass().getClassLoader(),
                false,
                dotcmsLoggerContext,
                dotcmsLoggerContext.getConfigLocation());

        //Initializing services...
        initializeServices(bundleContext);

        //Registering the hubspot Actionlet
        registerActionlet(bundleContext, new ContactSave());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        unregisterActionlets();

        //Shutting down log4j in order to avoid memory leaks
        Log4jUtil.shutdown(pluginLoggerContext);
    }

}