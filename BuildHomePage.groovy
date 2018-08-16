import groovy.text.*;

def indexFile = new File('index.html');
def srcDir = new File('src');
def src = "templates pages announcement news".split(/\s+/).collect {[key: it, value: new File(srcDir, it)]}.inject ([:]) {
  m, v -> m[v.key] = v.value;
  return m;
}

// Dig out the announcements
def announcementTemplateFile = new File(src.templates, 'announcements.template.html');
def annTemplate = new SimpleTemplateEngine()
                    .createTemplate(announcementTemplateFile.text);
def announcementSrc = src.announcement.list().grep {
  (it =~ /^\d{4}-\d{2}-\d{2}.*\.xml$/)
}.sort().collect{new File(src.announcement, it)};

def announcementList = announcementSrc.collect {
  announcementFile ->
  new XmlSlurper().parse(announcementFile);
}
String announcements = annTemplate.make([announcements: announcementList]);
// Create the news

// Create the home page

def indexTemplateFile = new File(src.templates, 'homePage.template.html');
assert indexTemplateFile.exists();
def indexTemplate = new SimpleTemplateEngine()
  .createTemplate(indexTemplateFile.text);

indexFile.withWriter {
  w ->
  indexTemplate.make([announcements: announcementList,
                      news: ""]).toString().readLines().each {
    w.println it
    // println it;
  }
}
