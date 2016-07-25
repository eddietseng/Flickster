# Flickster
Movie databse read-only android application

This is an Android Movie database read-only application

Time spent: 24 hours spent in total

Completed user stories:

 * [x] User can view a list of movies (title, poster image, and overview) currently playing in theaters from the Movie Database API.
 * [x] Lists should be fully optimized for performance with the ViewHolder pattern.
 * [x] Views should be responsive for both landscape/portrait mode.
 *  In portrait mode, the poster image, title, and movie overview is shown.
 *  In landscape mode, the rotated layout should use the backdrop image instead and show the title and movie overview to the right of it.
 
Optional user stories:
 * [x] Add pull-to-refresh for popular stream with SwipeRefreshLayout.
 * [x] Display a nice default placeholder graphic for each image during loading (read more about Picasso).
 * [x] Improve the user interface through styling and coloring.
 * Hided ActionBar
 * [x] For popular movies (i.e. a movie voted for more than 5 stars), the full backdrop image is displayed. Otherwise, a poster image, the movie title, and overview is listed. Use Heterogenous ListViews and use different ViewHolder layout files for popular movies and less popular ones.
 * [x] Stretch: Expose details of movie (ratings using RatingBar, popularity, and synopsis) in a separate activity.
 * [x] Stretch: Allow video posts to be played in full-screen using the YouTubePlayerView.
 * When clicking on a popular movie (i.e. a movie voted for more than 5 stars) the video should be played immediately.
 * Less popular videos rely on the detailed page should show an image preview that can initiate playing a YouTube video.
 * [x] Stretch: Add a play icon overlay to popular movies to indicate that the movie can be played.
 * [x] Stretch: Leverage the data binding support module to bind data into one or more activity layout templates.
 * [x] Stretch: Add a rounded corners for the images using the Picasso transformations.

Additional:
* [x] Replace ListView with RecyclerView.
 

Walkthrough of all user stories:

![Video Walkthrough]()

GIF created with [LiceCap](http://www.cockos.com/licecap/).

