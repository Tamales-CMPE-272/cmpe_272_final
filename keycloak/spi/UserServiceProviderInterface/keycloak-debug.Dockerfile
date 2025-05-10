FROM ubuntu:22.04

# Install tools
RUN apt-get update && \
    apt-get install -y curl unzip mysql-client netcat openjdk-21-jdk && \
    apt-get clean

# Install Keycloak
ENV KEYCLOAK_VERSION=26.2.3
RUN curl -L -o keycloak.zip https://github.com/keycloak/keycloak/releases/download/${KEYCLOAK_VERSION}/keycloak-${KEYCLOAK_VERSION}.zip && \
    unzip keycloak.zip && \
    mv keycloak-${KEYCLOAK_VERSION} /opt/keycloak && \
    rm keycloak.zip

ENV PATH="/opt/keycloak/bin:$PATH"
WORKDIR /opt/keycloak


COPY ./build/quarkus-app/app/*.jar /opt/keycloak/providers

COPY ./build/quarkus-app/lib/main/io.smallrye.jandex-3.1.6.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/com.mysql.mysql-connector-j-8.3.0.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/io.quarkus.quarkus-jdbc-mysql-3.8.4.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/com.fasterxml.jackson.core.jackson-annotations-2.16.1.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/com.fasterxml.jackson.core.jackson-core-2.16.1.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/com.fasterxml.jackson.core.jackson-databind-2.16.1.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/com.fasterxml.jackson.dataformat.jackson-dataformat-cbor-2.16.1.jar /opt/keycloak/providers
COPY ./build/quarkus-app/lib/main/com.fasterxml.jackson.datatype.jackson-datatype-jdk8-2.16.1.jar /opt/keycloak/providers


# Register the provider for build-time augmentation
ENV KC_SPI_USER_STORAGE_PROVIDER=cmpe272.tamalesHr.MySQLUserStorageProviderFactory

# Set database environment
ENV KC_DB=mysql
ENV KC_DB_URL=jdbc:mysql://host.docker.internal:3306/keycloak
ENV KC_DB_USERNAME=root
ENV KEYCLOAK_LOGLEVEL=DEBUG

RUN /opt/keycloak/bin/kc.sh build --verbose

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start-dev"]