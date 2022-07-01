FROM python:3.8-alpine

ENV PYTHONUNBUFFERED 1
COPY ./requirements.txt /requirements.txt
RUN apk add --update --no-cache --virtual .tmp-build-deps \ 
    gcc libc-dev linux-headers postgresql-dev musl-dev zlib zlib-dev
RUN pip install -r /requirements.txt
RUN apk add gcc musl-dev mariadb-connector-c-dev
RUN apk add mysql-client
RUN pip3 install mysqlclient
RUN apk del .tmp-build-deps

RUN mkdir /app
COPY ./app /app
WORKDIR /app