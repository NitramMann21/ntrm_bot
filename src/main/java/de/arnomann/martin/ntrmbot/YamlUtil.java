package de.arnomann.martin.ntrmbot;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class YamlUtil {

    private static File file;
    private static Yaml yaml;
    private static YamlData data;

    public static void load() throws IOException {
        file = new File("datenbank.yml");

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        yaml = new Yaml(options);

        if (file.exists())
            data = yaml.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        else {
            file.createNewFile();
            data = new YamlData();
        }

        saveYAML();
    }

    public static void saveYAML() {
        try {
            yaml.dump(data, new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        } catch (YAMLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<YamlData.Application> getApplications() {
        return data.applications;
    }

    public static YamlData.Application getApplicationByMessage(Message message) {
        List<YamlData.Application> applications = getApplications();
        for (YamlData.Application application : applications) {
            if (application.getMessageId() == message.getIdLong())
                return application;
        }
        return null;
    }

    public static YamlData.UploadPlan getUploadPlan() {
        return data.uploadPlan;
    }

    public static class YamlData {
        private List<Application> applications;
        private UploadPlan uploadPlan;

        public YamlData() {
            applications = new ArrayList<>();
            uploadPlan = new UploadPlan();
        }

        public List<Application> getApplications() {
            return applications;
        }

        public void setApplications(List<Application> applications) {
            this.applications = applications;
        }

        public UploadPlan getUploadPlan() {
            return uploadPlan;
        }

        public void setUploadPlan(UploadPlan uploadPlan) {
            this.uploadPlan = uploadPlan;
        }

        public static class Application {
            private long userId;
            private long roleId;
            private long messageId;

            public Application() {
                userId = 0L;
                roleId = 0L;
                messageId = 0L;
            }

            public Application(long userId, long roleId, long messageId) {
                this.userId = userId;
                this.roleId = roleId;
                this.messageId = messageId;
            }

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public long getRoleId() {
                return roleId;
            }

            public void setRoleId(long roleId) {
                this.roleId = roleId;
            }

            public long getMessageId() {
                return messageId;
            }

            public void setMessageId(long messageId) {
                this.messageId = messageId;
            }
        }

        public static class UploadPlan {
            private List<Upload> uploads;

            public UploadPlan() {
                uploads = new ArrayList<>();
            }

            public void setUploads(List<Upload> uploads) {
                this.uploads = uploads;
            }

            public List<Upload> getUploads() {
                return uploads;
            }

            public void addUpload(Upload upload) {
                if(upload != null && !uploads.contains(upload)) {
                    if(uploads.size() >= 5) {
                        uploads.remove(getOldestUpload());
                    }
                    uploads.add(upload);
                }
            }

            public Upload getNewestUpload() {
                Upload newestUpload = null;
                for(Upload upload : uploads) {
                    if(newestUpload == null || upload.getTime() > newestUpload.getTime())
                        newestUpload = upload;
                }
                return newestUpload;
            }

            public Upload getOldestUpload() {
                Upload oldestUpload = null;
                for(Upload upload : uploads) {
                    if(oldestUpload == null || upload.getTime() < oldestUpload.getTime())
                        oldestUpload = upload;
                }
                return oldestUpload;
            }

            public boolean deleteOldUploads() {
                List<Upload> uploadsBeforeDeleting = new ArrayList<>(uploads);
                uploads.removeIf(upload -> upload.getTime() < System.currentTimeMillis() / 1000);
                uploads.sort(Upload::compareTo);
                return !uploadsBeforeDeleting.equals(uploads);
            }

            public static LocalDateTime longToDateTime(long time) {
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());
            }

            public static class Upload implements Comparable<Upload> {
                private String project;
                private String content;
                private long time;

                public Upload() {
                    project = "";
                    content = "";
                    LocalDateTime now = LocalDateTime.now();
                    time = now.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(now));
                }

                public Upload(String project, String content, LocalDateTime time) {
                    this.project = project;
                    this.content = content;
                    this.time = time.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
                }

                public String getProject() {
                    return project;
                }

                public void setProject(String project) {
                    this.project = project;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                @Override
                public int compareTo(@NotNull YamlUtil.YamlData.UploadPlan.Upload o) {
                    return Long.compare(time, o.time);
                }
            }
        }
    }

}
