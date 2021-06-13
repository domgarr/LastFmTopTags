# UI-Challenge
##
The following project was built for an automotive company's interviewing process.

## Installation
Clone or download ZIP and simply open in Android Studios.

If Unsupported Module Detected try:
- File -> Invalidate Caches and Restart OR delete .idea directory and .iml file and re-open the project.


## Features
### Portrait Mode
- An empty fragment is displayed upon opening the app. Clicking the hamburger icon displays the list of top tags in order by popularity in a menu drawer.
- Clicking a tag will replace the empty fragment with a TrackFragment and update the title in the app bar with the chosen tag.
- TrackFragment with fetch top tracks for the chosen tag for display in a recycler view list.
- Choosing an item in TrackFragment will highlight the item (Indicating that it's being played) 
- A Tag or Track chosen in either view will persist through orientation change.

### Landscape Mode
- Two fragments are rendered when the app is opened or when oriented from protrait to landscape mode. The first fragment (TagFragment) will contain top tags in order by popularity. The second fragment (Track Fragment) will contain the tracks pertaining to that tag.
- Clicking a tag in the Tag Fragment will notify the listener in the main activty to - communicate to Track Fragment that tag has changed and to update the app bar with newly chosen tag.
- A Tag or Track chosen in either view will persist through orientation change.

### Last FM API
- Data for the lists are fetched via the Last FM API.
- Retrofit and rxJava are used in combination to perform GET requests.
- Top Tags  : https://www.last.fm/api/show/tag.getTopTags
- Top Tracks  : https://www.last.fm/api/show/tag.getTopTracks

#### Top Tags
- 50 of the top tags (ordered by descending count) are fetched.

#### Top Tracks
- A limit of 15 tracks are fetched by use of the limit query.

