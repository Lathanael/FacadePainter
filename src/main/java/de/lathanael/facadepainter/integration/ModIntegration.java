/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.integration;

import de.lathanael.facadepainter.integration.jei.FacadePainterRecipeLists;
import de.lathanael.facadepainter.integration.jei.JEIBlacklistUpdater;
import mezz.jei.api.IModRegistry;
import net.minecraftforge.fml.common.Loader;

public class ModIntegration {

    public static FacadePainterRecipeLists recipeList;
    public static JEIBlacklistUpdater jeiBlacklistUpdater;
    public static boolean isJEILoaded = false;

    public static void preInitJEI(IModRegistry registry) {
        isJEILoaded = Loader.isModLoaded("jei");
        if(isJEILoaded) {
            recipeList = new FacadePainterRecipeLists(registry);
            jeiBlacklistUpdater = new JEIBlacklistUpdater();
        }
    }

    public static void updateJEIRecipeList() {
        if(isJEILoaded) {
            jeiBlacklistUpdater.handleBlacklisting();
        }
    }
}