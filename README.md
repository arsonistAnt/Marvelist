# Marvelist
A  Marvel comic book tracker that tracks reading progress on physical or digital comics. Video demo [here](https://youtu.be/69d_HMlNnNU).


[![Marvelist Demo](https://j.gifs.com/P799Gn.gif)](https://youtu.be/69d_HMlNnNU)

### Features Include:
- Browsing for comics (sorted by the latest modified)
- Saving comics to the reading list (via long press)
- Updating the reading progress for each comics.
- A detailed page for the comic (e.g. author name, credits, sale date).

# Usage
![](https://i.imgur.com/ABJoIno.jpg?1)

- #### How do  you load more comics?
  - In the _Browse_ section, the app will automatically load for more comics from the network as it scrolls down. (Paging/Pagination)

- #### How to save a comic?
  - Simply long press a comic in the _Browse_ section of the app.
    A message will pop up saying that "Comic Title has been added to the list".

- #### How to update reading progress of a comic?
  - Press any of the _Reading_, _Unread_, and _Read_ chips in the reading list section. Note:  The reading list will be empty if you've     havent added a comic yet.
  - No save button needed, once the chips have been pressed the reading progress is saved.
  
# Architecture and Navigation Structure Summary
  This app was created with the MVVM architecture and a "1 Activity hosting many fragments" navigation structure (Navigation Component).
### Model-View-Viewmodel Overview
  - The _Model_ layer contains a repository of data that can be from the network or the local database. Only the _ViewModel_ knows about the _Model_ layer.
  - The _View_ observes data that's been exposed/emitted from the _View Model_ and updates the UI accordingly. This layer only knows about the _View Model_.
  - The _View Model_ exposes and formats data from the _Model_ layer and only knows about the _Model_.
  
### Navigation Overview
The [NavigationHostActivity](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/base/NavigationHostActivity.kt) is in charge of navigating/swapping out the different fragments (via [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)). By default the _Browse_ fragment is shown first, the user can navigate to the _Browse_ and _Reading List_ section of the app through the navigation drawer. The _Comic Detail_ section can only be accessed through the _Browse_ or _Reading List_ fragments as shown below:

[Navigation Graph Image]: <> (Display image of the navigation graph.)
<p align="center">
  <img src="https://i.imgur.com/2NwMtUH.png" />
</p>

## View
There are three different _Views_ in this app's architecture and they are the [BrowseFragment](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/comicbrowser/BrowseFragment.kt), [ComicDetailsFragment](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/comicdetails/ComicDetailsFragment.kt), and the [ReadingListFragment](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/readinglist/ReadingListFragment.kt). All these Fragments observe their respective _ViewModels_ and display the data that's been observed. The reference graph can be seen below:

[View Graph Image]: <> (Display image of the View's graph and relationship to its ViewModels.)
<p align="center">
  <img src="https://i.imgur.com/PLqzRXU.png" />
</p>

    
#### BrowseFragment
- Lazily loads a list of preview of comics (picture, title, description) in the UI.
- Handles long press interaction (Long press to save a comic to the reading list)

#### ComicDetailsFragment
- Shows a more detailed represenation of the comic thats been tapped on from the _Browse_ or _Reading List_ section.
- This Fragment request more info about the comic from its ViewModel to display into the UI.
- Displays author's name, list of creators involved in the comics, on sale date, foc date, etc.

#### ReadingListFragment
- Shows a list of comics that's been saved to the local database.
- Each comic item displays a group of interactable [Chips](https://material.io/components/chips/) that is used to track reading progress.
- Allows deletion of comics in the reading list.

## View Model
There are three different _View Models_ the [BrowseViewModel](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/viewmodel/BrowseViewModel.kt), [ComicDetailsViewModel](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/viewmodel/ComicDetailsViewModel.kt), and the [ReadingListViewModel](https://github.com/arsonistAnt/Marvelist/blob/master/app/src/main/java/com/example/marvelist/ui/viewmodel/ReadingListViewModel.kt). The purpose of these models is to expose data that's relevant to the UI. For example the BrowseViewModel will retrieve/expose a data source for the purpose of loading comic book data in chunks.

[View Model Graph Image]: <> (Display image of the ViewModels graph)
<p align="center">
  <img src="https://i.imgur.com/zdbzdxa.png" />
</p>

#### BrowseViewModel
- Retrieves the comic data source that emits comic data in chunks via [Pagination Library](https://developer.android.com/topic/libraries/architecture/paging)
- Tells the _Model_ layer to save a specific comic book to the database.

#### ComicDetailsViewModel
- Requests comic book info from the _Model_ layer.

#### ReadingListViewModel
- Requests a list of saved comic books from the _Model_ layer.
- Can request to remove certain comic books by id from the database.
- Can request update changes to the reading progress property for a comic in the database. 

