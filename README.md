<h1 align="center">FamilyApp <br /> (abandoned)</h1>
<p align="center">
  <a href="https://github.com/RomaZykov/FamilyApp/blob/main/README.md">
    <img src="https://img.shields.io/badge/lang-en-yellow" />
  </a>
  <a href="https://github.com/RomaZykov/FamilyApp/blob/main/README.ru.md">
    <img src="https://img.shields.io/badge/%D1%8F%D0%B7%D1%8B%D0%BA-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9-orange" />
  </a>
</p>
<p align="center">

## Description

A gamified task tracker mobile app for parents and their children. Parents set
goals with a certain amount (10 points maximum) for each child, tasks are defined to complete
the goal (no more than 3 points), and children complete tasks and thus close large goals and
receive a reward for the goal (or do not receive). 

The child marks the completed task in his profile for
verification by the parent -> the parent confirms/does not confirm the completion of the task -> task points
are awarded to the attached goal.

## Screenshots
<p align="center">
<img src="https://github.com/RomaZykov/FamilyApp/blob/main/demo/FamilyApp%20demo%201.png">
</p>
<p align="center">
<img src="https://github.com/RomaZykov/FamilyApp/blob/main/demo/FamilyApp%20demo%202.png">
</p>
<p align="center">
<img src="https://github.com/RomaZykov/FamilyApp/blob/main/demo/FamilyApp%20demo%203.png">
</p>

## Stack

- Kotlin, Kotlin coroutines
- MVVM, an attempt at Clean Architecture
- Single-Activity
- XML
- Datastore
- Picasso
- Dagger2
- Firebase: Auth, Realtime Database
- Androix: ViewPager2, RecyclerView, ViewModel, LiveData/Flow

## Features

- Separate profile for parent and child with password switching
- Create a goal with points (maximum 10)
- Create tasks (maximum 3 points) within the goal
- Archive of completed goals and objectives
- Deleting the profile (destroying the data of the parent and all children)
- Adding a child
- Checking completed tasks
- Support for Russian and English languages

## Notes

- Design from Figma (made by designers)
- Network-first approach
- There is an attempt to test RecyclerView and a little bit of ViewModel
- 99% completed publication on Google Play

## Post-mortem

As Frederick Brooks wrote in his famous book "The Mythical Man-month": "Plan to throw one away; you will, anyhow," the uncle was damn right, so this punishment overtook my first program (you can say,
the version). I am currently working on my second application with multi-modularity and good practices:
tests, OOP, etc.

- Based on the example of a project with a bad architecture
- Dispatchers in the ViewModel are nailed down, not passed through DI
- 1% Test coverage (kringe, wanted 70%) within the framework of the classical school of unit testing
- A single Network model for all layers (what was I even thinking about?) -> working local Room model
  absent
- Leakage of the business logic of registration in a fragment (in the second project everything is beautiful)
- There are hardcode elements
- The presence of both user cases and repositories in the domain layer at the same time
- Availability of both flow and live data at the same time
- Commented out code
- Did not output duplicate code to the abstract entities
- Overcomplicated ViewModel - did not encapsulate logic in another layer
