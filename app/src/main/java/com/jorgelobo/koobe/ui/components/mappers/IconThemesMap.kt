package com.jorgelobo.koobe.ui.components.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.icons.IconThemes

@Composable
fun iconThemesMap(): Map<String, List<ImageVector>> {
    return mapOf(
        stringResource(R.string.arrows) to IconThemes.Arrows,
        stringResource(R.string.art) to IconThemes.Art,
        stringResource(R.string.clothing_accessories) to IconThemes.ClothingAndAccessories,
        stringResource(R.string.family_people) to IconThemes.FamilyAndPeople,
        stringResource(R.string.finance_shopping) to IconThemes.FinanceAndShopping,
        stringResource(R.string.food_drink) to IconThemes.FoodAndDrink,
        stringResource(R.string.health) to IconThemes.Health,
        stringResource(R.string.home_life) to IconThemes.HomeAndLife,
        stringResource(R.string.logos) to IconThemes.Logos,
        stringResource(R.string.math_science) to IconThemes.MathAndScience,
        stringResource(R.string.music_sound) to IconThemes.MusicAndSound,
        stringResource(R.string.nature_animals) to IconThemes.NatureAndAnimals,
        stringResource(R.string.photo_video) to IconThemes.PhotoAndVideo,
        stringResource(R.string.places_building) to IconThemes.PlacesAndBuildings,
        stringResource(R.string.shapes) to IconThemes.Shapes,
        stringResource(R.string.sport_leisure) to IconThemes.SportAndLeisure,
        stringResource(R.string.symbols) to IconThemes.Symbols,
        stringResource(R.string.technology_communications) to IconThemes.TechnologyAndCommunications,
        stringResource(R.string.time) to IconThemes.Time,
        stringResource(R.string.tools) to IconThemes.Tools,
        stringResource(R.string.transport_travel) to IconThemes.TransportAndTravel,
        stringResource(R.string.work_business) to IconThemes.WorkAndBusiness
    )
}