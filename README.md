### Table of Contents
1. [About](#about)
4. [Package Contents](#package-contents)
5. [Issues](#issues)

<br>
<br>

### About <a name="about"></a> 
Sync files across directories.

<br>
<br>

### Package Contents <a name="package-contents"></a> 

| File         | Description |
|---------------|-------------|
| artifacts/SyncDirectory | A directory that is registered to a SyncEntity.|
| artifacts/SyncEntity | A collection of directories that are being syncronized.|
| shell/ | Contains .sh files |
| Execute       | Issues sh commands.|
| Main          | Run from here.|
| Routines      | Contains higher level routines.|
| Tools         | Contains lower level tools that are used by other classes.|

<br>
<br>

### Issues <a name="issues"></a> 

- Create a parallel Thread for each SyncEnity.
- Add support for modification dates. 
  - And thereby eventually support 10 out of 10 file operation types.
- Reduce disk access.
- Have some error handling. (i.e. if a web-directory is not available)
- Create a UI.
- Start the program at system start.
