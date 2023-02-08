package com.example.feedservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfig  extends AbstractCassandraConfiguration{

    public String getContactPoints() {
        return "localhost";
    }

    public String getKeyspaceName() { return "insta_feed";}
}