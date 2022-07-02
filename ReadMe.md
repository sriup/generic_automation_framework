## Post Modification Steps
- Run `mvn clean install`
- The above command will create a folder in local maven repository under specified `groupId` and `artifactId`
  with the provided version number.
- Compress the `groupId` folder and share the updated folder with the team.

- Team has to extract the zip.

- <font color='red'>If the folder already exist then team has to replace/paste
  only the `artifactId` based folder rather the `groupId` folder. This will make sure you don't lose
  any other dependencies with the same <b>groupID</b></font>.

- If there is no folder exists with the `groupId` then they can simply place the entire folder under their
  local maven repository in the specified path.