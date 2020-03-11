### Table of Contents
1. [About](#about)
4. [Package Contents](#package-contents)
5. [Issues](#issues)

<br>
<br>

### About <a name="about"></a> 
Sync files across directories.

![alt text](https://raw.githubusercontent.com/IO42630/ensync/master/doc/flow-n-instances.png "Hello!")
<br>
<br>

### Package Contents <a name="package-contents"></a> 

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

### Issues <a name="issues"></a> 

##### High Prio

- Have Map entries be remove, once file ops is performed.
- Create a parallel Thread for each SyncEnity.
- Add support for modification dates. 
  - And thereby eventually support 10 out of 10 file operation types.
- Reduce disk access.
- Have some error handling. (i.e. if a web-directory is not available)
- Create a UI.
- Start the program at system start.
- Track files that were modified during the loop.
    - currently `writeStateFile` just takes from `find`
    - this means any changes made during the loop will be written to the `StateFile`
    - and created files are tracked by comparing `StateFile` (=old state) and `State` (=new state).
    - because of this it will appear as if the file created while the loop was running
    was already there.
    - thus the creation of said file will not be replicated to the other directories.
    - to solve this `writeStateFile` should take the old `State` and manually add every operation that was performed by the loop (!= user created file while the loop was running).
    - however this will be done later . . maybe.

      
##### Medium Prio
- If file is deleted in DirA and DirB, then two delete commands will be issued.
    - They will both return errors and effectively do nothing.
    - However this is a dirty solution.
    - Fix this by checking if deleted file of DirA exists in DirB.listDeleted
    - To do so .listDeleted would need to be a field of Dir
    - And the .lists of every dir would need to be calculated before any deletion took place.
    - Check if the reduced reobustness is worth the prettier solution.
 - File is created in DirB
    - Sync creates the file in DirA
    - Sync creates the file in DirB 
      - this means the file in DirB is overwritten with `cp` for no reason.
      - implement a check to prevent this.
      
      
##### Low Prio