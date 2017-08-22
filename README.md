
# Pre-work - *Cinderelly*

**Cinderelly** is an android app that allows building a todo list and basic todo items management functionality including adding, editing and deleting items.

Submitted by: **Jayson Dela Cruz**

Time spent: **12** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Add the ability to mark tasks as complete

## Video Walkthrough

Here's a walkthrough of implemented user functions:

<img src="http://i.imgur.com/eWq8Vm9.gif" alt="GIF Walkthrough" width="300"/>

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** The way Android approaches layouts as XML files makes it very easy to reuse/repurpose components. The layout editor/preview built into Android Studio helps a lot when it comes to positioning items, but switching between the drag-and-drop editor and XML is a bit finicky as a lot of code is auto-generated.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** The `ArrayAdapter` is used to populate the `ListView` using an array of items. Adapters in general are important because they specify how data gets translated into `Views` for the user.

The `getView` method in `ArrayAdapter` provides a `convertView` object which is a reference to a `View` object that can be recycled. Instead of creating an entirely new `View` to populate, you can simply overwrite the `convertView` with new data to improve performance. 

## License

    Copyright [2017] [Jayson Dela Cruz]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
