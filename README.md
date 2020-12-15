# FlowJo csv import

This plugin can annotate samples in FlowJo using additional data in the form of csv files.

## usage

If a file is specified, the samples in the FlowJo workspace are annotated with additional keywords.
The plugin expects a csv file with the following header:

```
Name, Attribute1, Attribute2 ...
```

After the first column, the attributes can be arbitrarily named and the
column names will be used as keywords for sample annotation. 
This should allow us to add perturbations, subject ids, genotype data etc in a flexible fashion.

Note: Just hit cancel to not load anything.

## installation

Download the plugin from the git repo and save to your computer, ideally in FlowJo's plugins folder:
```
flowjo_csv_import/out/artifacts/flowjo_csv_import_jar/flowjo_csv_import.jar
```

Adjust path for FlowJo to find the file where you saved it:
```
FlowJo > Preferences > Diagnostics > search path to JAR
```

You might have to restart FlowJo now. Then add the plugin:
```
Workspace > Plugins > Add Workspace Plugin > Select "CSVImport" and click add
```

## development

I used IntelliJ for this. You need to add `fjlib-2.4.0.jar` to the project libraries:

```
External Library Settings > Add > select fjlib.X.Y.Z.jar
```

Tell IntelliJ to build a JAR file when building the project:
```
File > Project Structure > Artifacts > Add Jar > From modules with dependencies > ImportCSV (no main class)
```

Hit build button (green hammer).
