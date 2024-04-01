package de.christcoding.beerfellow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import de.christcoding.beerfellow.R
import de.christcoding.beerfellow.model.Breed
import de.christcoding.beerfellow.ui.theme.Background
import de.christcoding.beerfellow.ui.theme.Primary
import de.christcoding.beerfellow.ui.theme.Secondary

@Composable
fun BeerListItem(beer: Breed, showDetails: () -> Unit) {
        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Primary, shape = RoundedCornerShape(16.dp))) {
            if(beer.image != null)
                AsyncImage(model = beer.image!!.url, contentDescription = "Beer Image", modifier = Modifier.size(120.dp).padding(8.dp),
                    placeholder = painterResource(R.drawable.baseline_cloud_off_24), error = painterResource(R.drawable.baseline_cloud_off_24))
            else
                Icon(painter = painterResource(R.drawable.baseline_cloud_off_24), contentDescription = "Beer Image", modifier = Modifier.size(100.dp).padding(8.dp))
            Column (verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.size(width = 200.dp, height = 120.dp)){
                Text(text = beer.name, color = Background, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
                if(beer.origin != null)
                    Text(text = beer.origin, color = Secondary)
            }
            IconButton(modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .background(Secondary), onClick = { showDetails() }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Details", tint = Background)
            }
        }
}