import groovy.text.*;

def indexFile = new File('index.html');
def srcDir = new File('src');
def src = "templates pages announcement news".split(/\s+/).collect {[key: it, value: new File(srcDir, it)]}.inject ([:]) {
  m, v -> m[v.key] = v.value;
  return m;
}

// Dig out the announcements

// Create the news

// Create the home page

def indexTemplateFile = new File(src.templates, 'homePage.template.html');
assert indexTemplateFile.exists();
def indexTemplate = new SimpleTemplateEngine()
  .createTemplate(indexTemplateFile.text);

indexFile.withWriter {
  w ->
  indexTemplate.make([announcements: "",
                      news: ""]).toString().readLines().each {
    w.println it
    // println it;
  }
}
