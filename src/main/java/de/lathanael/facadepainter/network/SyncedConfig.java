/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.network;

import de.lathanael.facadepainter.config.Configs;

public class SyncedConfig {

    public static boolean enableChamaeleoPaint = Configs.features.enableChamaeleoPaint;
    public static boolean hideJEIFacadePaintingRecipeCategory = Configs.features.hideJEIFacadePaintingRecipeCategory;
    public static boolean useChamaeleoPaint = Configs.recipes.useChamaeleoPaint;
    public static boolean enableShapelessClearingRecipe = Configs.recipes.enableShapelessClearingRecipe;

}