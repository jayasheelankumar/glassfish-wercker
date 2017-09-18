#!/bin/bash -e
build_re_dev(){
    build_init
    dev_build
    build_re_finalize
}

build_init(){
    init_common
    kill_glassfish
    print_env_info
    create_version_info
}

init_common(){
  
    PRODUCT_GF="glassfish"
    JAVAEE_VERSION=8.0
    MAJOR_VERSION=5
    MINOR_VERSION=0
    PRODUCT_VERSION_GF=${MAJOR_VERSION}.${MINOR_VERSION}
    MICRO_VERSION=
    if [ ! -z $MICRO_VERSION ] && [ ${#MICRO_VERSION} -gt 0 ]; then
        PRODUCT_VERSION_GF=$PRODUCT_VERSION_GF.${MICRO_VERSION} 
    fi
    
    MDATE=$(date +%m_%d_%Y)
    DATE=$(date)
    WORKSPACE=$PWD
    rm -rf ${WORKSPACE}/repository | true
    MAVEN_REPO="-Dmaven.repo.local=${WORKSPACE}/repository"
    MAVEN_ARGS="${MAVEN_REPO} -C -nsu -B"
    MAVEN_OPTS="${MAVEN_OPTS} -Xmx1024M -Xms256m -XX:MaxPermSize=512m -XX:-UseGCOverheadLimit"
    
    export JAVAEE_VERSION \
            MAJOR_VERSION \
            MINOR_VERSION \
            MICRO_VERSION \
            VERSION \
            BUILD_ID \
            PKG_ID \
            RELEASE_VERSION \
            PRODUCT_GF \
            PRODUCT_VERSION_GF \
            MDATE \
            DATE \
            IPS_REPO_URL \
            IPS_REPO_DIR \
            IPS_REPO_PORT \
            IPS_REPO_TYPE \
            PROMOTED_BUNDLES \
            GF_WORKSPACE_URL_SSH \
            GF_WORKSPACE_URL_HTTP \
            MAVEN_OPTS \
            MAVEN_REPO \
            MAVEN_ARGS \
            WORKSPACE
}

kill_glassfish(){
    kill_clean `jps | grep ASMain | awk '{print $1}'`
}

print_env_info(){
    printf "\n%s \n\n" "==== ENVIRONMENT INFO ===="
    pwd
    uname -a
    java -version
    mvn --version
    git --version
}

create_version_info(){
    printf "\n%s\n\n" "==== VERSION INFO ===="
    CURRENT_COMMIT=`git rev-parse --short HEAD`
    echo Git Revision:  ${CURRENT_COMMIT} > ${WORKSPACE}/version-info.txt
    echo Git Branch: `git branch | grep ^\* | cut -d ' ' -f 2` >> ${WORKSPACE}/version-info.txt
    echo Build Date: `date` >> ${WORKSPACE}/version-info.txt
    export CURRENT_COMMIT
}

dev_build(){
    printf "\n%s \n\n" "===== DO THE BUILD! ====="
    mvn ${MAVEN_ARGS} -f pom.xml clean install \
        -Dmaven.test.failure.ignore=true
}

build_re_finalize(){
    archive_bundles
    zip_tests_maven_repo
    zip_gf_source
}

archive_bundles(){
    printf "\n%s \n\n" "===== ARCHIVE BUNDLES ====="
    rm -rf ${WORKSPACE}/bundles ; mkdir ${WORKSPACE}/bundles
    cp ${WORKSPACE}/version-info.txt $WORKSPACE/bundles
    cp ${WORKSPACE}/appserver/distributions/glassfish/target/*.zip ${WORKSPACE}/bundles
    cp ${WORKSPACE}/appserver/distributions/web/target/*.zip ${WORKSPACE}/bundles
    cp ${WORKSPACE}/nucleus/distributions/nucleus/target/*.zip ${WORKSPACE}/bundles
}

zip_tests_maven_repo(){
    printf "\n%s \n\n" "===== ZIP PART OF THE MAVEN REPO REQUIRED FOR TESTING ====="
    pushd ${WORKSPACE}/repository

    # ideally this should be done
    # from a maven plugin...
    zip -r ${WORKSPACE}/bundles/tests-maven-repo.zip \
        org/glassfish/main/common/* \
        org/glassfish/main/grizzly/* \
        org/glassfish/main/glassfish-nucleus-parent/* \
        org/glassfish/main/test-utils/* \
        org/glassfish/main/tests/* \
        org/glassfish/main/admin/* \
        org/glassfish/main/core/* \
        org/glassfish/main/deployment/deployment-common/* \
        org/glassfish/main/deployment/nucleus-deployment/* \
        org/glassfish/main/external/ldapbp-repackaged/* \
        org/glassfish/main/external/nucleus-external/* \
        org/glassfish/main/flashlight/flashlight-framework/* \
        org/glassfish/main/grizzly/grizzly-config/* \
        org/glassfish/main/grizzly/nucleus-grizzly-all/* \
        org/glassfish/main/security/security/* \
        org/glassfish/main/security/security-services/* \
        org/glassfish/main/security/ssl-impl/* \
        org/glassfish/main/admingui/* \
        org/glassfish/main/glassfish-parent/* \
        org/glassfish/main/security/nucleus-security/* > /dev/null
    popd
}

zip_gf_source(){
    printf "\n%s \n\n" "===== ZIP THE SOURCE CODE ====="
    zip -r ${WORKSPACE}/bundles/main.zip ./ -x **/target\* > /dev/null
}

kill_clean(){
    if [ ${#1} -ne 0 ]
    then
        kill -9 ${1} || true
    fi
}
