docker login --username eliezerjg --password %GITHUB_PACKAGE_PW% ghcr.io
docker build . -t ghcr.io/eliezerjg/gamecms-backend-platform:latest
docker push ghcr.io/eliezerjg/gamecms-backend-platform:latest