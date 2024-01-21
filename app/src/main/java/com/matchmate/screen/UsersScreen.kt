package com.matchmate.screen

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.matchmate.R
import com.matchmate.R.drawable.female
import com.matchmate.R.drawable.male
import com.matchmate.model.User
import com.matchmate.model.User.Companion.ACCEPTED
import com.matchmate.model.User.Companion.GENDER_MALE
import com.matchmate.model.User.Companion.PENDING
import com.matchmate.model.User.Companion.REJECTED
import com.matchmate.utils.Resource
import com.matchmate.viewmodel.UserViewModel

@Composable
fun UserScreen() {
    val usersViewModel: UserViewModel = hiltViewModel()
    val usersResource: State<Resource<List<User>>> = usersViewModel.users.collectAsState()

    when (usersResource.value) {
        is Resource.Loading -> {
            CircularProgress()
        }

        is Resource.Error -> {
            NoDataView(message = stringResource(id = R.string.no_data_found))
        }

        is Resource.Success -> {
            usersResource.value.data?.let {
                LazyColumn {
                    items(it) {
                        UserItem(user = it) { status ->
                            usersViewModel.updateUserStatus(status, it.id)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserItem(user: User, onStatusChange: (Int) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .shadow(2.dp)
            .padding(12.dp, 16.dp)
    ) {
        val (image, name, age, gender, location, status) = createRefs()

        // Profile Image.
        GlideImage(
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(100.dp)
                .clip(RoundedCornerShape(100.dp)),
            model = user.picture.large,
            contentDescription = user.name.first
        )

        // First Name.
        Text(
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(image.top)
                    start.linkTo(image.end)
                }
                .padding(12.dp, 0.dp),
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(id = R.string.formatted_name, user.name.first, user.name.last),
            textAlign = TextAlign.Start)

        // Gender.
        Image(
            modifier = Modifier
                .constrainAs(gender) {
                    top.linkTo(name.bottom)
                    start.linkTo(image.end)
                }
                .padding(12.dp, 0.dp)
                .size(20.dp),
            painter = painterResource(id = if (user.gender == GENDER_MALE) male else female),
            contentDescription = user.gender
        )

        // Age.
        Text(
            modifier = Modifier
                .constrainAs(age) {
                    start.linkTo(gender.end)
                    top.linkTo(gender.top)
                    bottom.linkTo(gender.bottom)
                },
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(id = R.string.formatted_age, user.dob.age),
            textAlign = TextAlign.Start
        )

        // Location.
        Text(
            modifier = Modifier
                .constrainAs(location) {
                    top.linkTo(gender.bottom)
                    start.linkTo(image.end)
                }
                .padding(12.dp, 8.dp),
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(
                id = R.string.formatted_location,
                user.location.city,
                user.location.state,
                user.location.country
            ),
            textAlign = TextAlign.Start)

        // Acceptance Status.
        if (user.status == PENDING) {
            Row(
                modifier = Modifier
                    .constrainAs(status) {
                        top.linkTo(location.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(image.end)
                    }
                    .padding(12.dp)) {
                ActionButton(
                    actionName = stringResource(id = R.string.accept),
                    actionIcon = R.drawable.ic_tick
                ) { onStatusChange(ACCEPTED) }
                ActionButton(
                    actionName = stringResource(id = R.string.reject),
                    actionIcon = R.drawable.ic_close
                ) { onStatusChange(REJECTED) }
            }
        } else {
            ActionButton(
                modifier = Modifier
                    .constrainAs(status) {
                        top.linkTo(location.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(image.end)
                    }
                    .padding(12.dp),
                actionName = if (user.status == ACCEPTED) stringResource(id = R.string.accepted)
                else stringResource(id = R.string.rejected),
                actionIcon = if (user.status == ACCEPTED) R.drawable.ic_tick else R.drawable.ic_close,
                fontColor = if (user.status == ACCEPTED) R.color.green else R.color.red,
                useBoldFont = true,
                action = {}
            )
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    actionName: String,
    @DrawableRes actionIcon: Int,
    @ColorRes fontColor: Int = R.color.black,
    useBoldFont: Boolean = false,
    action: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 2.dp, bottom = 2.dp, start = 0.dp, end = 20.dp)
            .clickable { action() }
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = actionIcon),
            contentDescription = actionName
        )

        Text(
            modifier = Modifier.padding(4.dp, 0.dp),
            text = actionName,
            color = colorResource(id = fontColor),
            fontWeight = if (useBoldFont) FontWeight.Bold else FontWeight.Normal
        )
    }
}