#### Table of Contents
1. [Demo](#demo)
1. [About](#about)
4. [Package Contents](#package-contents)
5. [Issues](#issues)

<br>
<br>

#### Demo<a name="demo"></a> 
[![IMAGE ALT TEXT](http://img.youtube.com/vi/znR3jyM_4Ss/0.jpg)](https://youtu.be/znR3jyM_4Ss "ensync WIP Demo")

<br>

#### About <a name="about"></a> 
Sync files across directories.

![alt text](https://raw.githubusercontent.com/IO42630/ensync/master/doc/flow-n-instances.png "Hello!")
<br>
<br>

#### Package Contents <a name="package-contents"></a> 

| Path         | Comment |
|---------------|-------------|
doc | Diagrams.
src.com.olexyn.ensync.artifacts | Data Model: Maps, Directories, Files. 
src.com.olexyn.ensync.shell | .sh files to ease interaction with the host.
src.com.olexyn.ensync.ui | JavaFX.
src.com.olexyn.ensync.Execute       | Issue .sh commands.
src.com.olexyn.ensync.Main          | Run from here.
src.com.olexyn.ensync.Flow      | Flow of the synchronization.
src.com.olexyn.ensync. | Low level helper methods.

<br>
<br>

#### Issues <a name="issues"></a> 

- Add tests.
- Remove Map entries, once file ops is performed.
- Reduce disk access.
- Add error handling. (i.e. if a web-directory is not available)
- Track files that were modified during the loop.
    - currently `writeStateFile` just takes from `find`
    - this means any changes made during the loop will be written to the `StateFile`
    - and created files are tracked by comparing `StateFile` (=old state) and `State` (=new state).
    - because of this it will appear as if the file created while the loop was running
    was already there.
    - thus the creation of said file will not be replicated to the other directories.
    - to solve this `writeStateFile` should take the old `State` 
    and manually add every operation that was performed by the loop (!= user created file while the loop was running).
 - File is created in DirB
    - Sync creates the file in DirA
    - Sync creates the file in DirB 
      - this means the file in DirB is overwritten with `cp` for no reason.
      - implement a check to prevent this.
