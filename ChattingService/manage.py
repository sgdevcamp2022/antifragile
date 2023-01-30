#!/usr/bin/env python
"""Django's command-line utility for administrative tasks."""
import os
import sys

"""eureka register"""
import py_eureka_client.eureka_client as eureka_client


def main():
    """Run administrative tasks."""
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "chatserver.settings")
    try:
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    execute_from_command_line(sys.argv)


# # server_host = "discovery"
# server_port = 9000


# def eureka_init():
#     eureka_client.init(
#         eureka_server="http://localhost:8761/eureka",
#         app_name="chatting-server",
#         instance_port=server_port,
#     )
#     print("Eureka client is running")


if __name__ == "__main__":
    # eureka_init()
    main()
