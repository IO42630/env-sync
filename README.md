### Table of Contents
1. [About](#about)
4. [Package Contents](#package-contents)
5. [Issues](#issues)

<br>
<br>

### About <a name="about"></a> 
Sync files across directories:
* with rsync
* using config file: sync.conf
* at system start-up (TODO)

<br>
<br>

### Package Contents <a name="package-contents"></a> 

| Class         | Description |
|---------------|-------------|
| Execute       | Issues com.olexyn.ensync.shell commands.|
| Main          | Main class. Run from here.|
| Routines      | Contains higher level routines.|
| Tools         | Simple tools used by other classes.|

<br>
<br>

### Issues <a name="issues"></a> 

- What about parallel Threads?
- What about error handling? (i.e. if a web-directory is not available)
- Make commands more expressive e.g. collection dir dir dir -> keep all 3 dirs on sync.
- Figure out a way to handle deletions.