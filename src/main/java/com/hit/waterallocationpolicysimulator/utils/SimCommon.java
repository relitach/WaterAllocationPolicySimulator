package com.hit.waterallocationpolicysimulator.utils;

import com.hit.waterallocationpolicysimulator.view.AboutController;
import com.hit.waterallocationpolicysimulator.view.ConfigurationController;
import com.hit.waterallocationpolicysimulator.view.MacroSimulationController;
import com.hit.waterallocationpolicysimulator.view.MicroSimulationController;


public class SimCommon
{
    private static SimCommon instance = null;

    private MicroSimulationController microSimulationController = MicroSimulationController.getInstance();
    private MacroSimulationController macroSimulationController = MacroSimulationController.getInstance();
    private ConfigurationController configurationController = ConfigurationController.getInstance();
    private AboutController aboutController = AboutController.getInstance();



    public static SimCommon getInstance() {
        if (instance == null) {
//            logger.debug("KorCommon init");
            instance = new SimCommon();
        }

        return instance;
    }

    public static void setInstance(SimCommon instance) {
        SimCommon.instance = instance;
    }

    public MicroSimulationController getMicroSimulationController() {
        return microSimulationController;
    }

    public void setMicroSimulationController(MicroSimulationController microSimulationController) {
        this.microSimulationController = microSimulationController;
    }

    public MacroSimulationController getMacroSimulationController() {
        return macroSimulationController;
    }

    public void setMacroSimulationController(MacroSimulationController macroSimulationController) {
        this.macroSimulationController = macroSimulationController;
    }

    public ConfigurationController getConfigurationController() {
        return configurationController;
    }

    public void setConfigurationController(ConfigurationController configurationController) {
        this.configurationController = configurationController;
    }

    public AboutController getAboutController() {
        return aboutController;
    }

    public void setAboutController(AboutController aboutController) {
        this.aboutController = aboutController;
    }
}
