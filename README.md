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
    



  

