FROM java:8
MAINTAINER email@example.com
VOLUME /tmp
EXPOSE 8080

ENV USER_NAME andrey
ENV APP_HOME /home/$USER_NAME/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD matmatch-app-0.1.0.jar $APP_HOME/matmatch-app.jar
RUN chown $USER_NAME $APP_HOME/matmatch-app.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN bash -c 'touch matmatch-app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","matmatch-app.jar"]
