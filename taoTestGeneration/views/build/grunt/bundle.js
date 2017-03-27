/**
 * Copyright (c) 2016-2017  Luxembourg Institute of Science and Technology (LIST).
 * 
 * This software is licensed under the Apache License, Version 2.0 (the "License") ; you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at : http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * for more information about the software, please contact info@list.lu
 */
module.exports = function(grunt) {

    var requirejs   = grunt.config('requirejs') || {};
    var clean       = grunt.config('clean') || {};
    var copy        = grunt.config('copy') || {};

    var root        = grunt.option('root');
    var libs        = grunt.option('mainlibs');
    var ext         = require(root + '/tao/views/build/tasks/helpers/extensions')(grunt, root);
    var out         = 'output';

    /**
	 * Remove bundled and bundling files
	 */
    clean.taoaigfacadebundle = [out];

    /**
	 * Compile tao files into a bundle
	 */
    requirejs.taoaigfacadebundle = {
        options: {
            baseUrl : '../js',
            dir : out,
            mainConfigFile : './config/requirejs.build.js',
            paths : { 'taoTestGeneration' : root + '/taoTestGeneration/views/js'},
            modules : [{
                name: 'taoTestGeneration/controller/routes',
                include : ext.getExtensionsControllers(['taoTestGeneration']),
                exclude : ['mathJax', 'mediaElement'].concat(libs)
            }]
        }
    };

    /**
	 * copy the bundles to the right place
	 */
    copy.taoaigfacadebundle = {
        files: [
            { src: [out + '/taoTestGeneration/controller/routes.js'],  dest: root + '/taoTestGeneration/views/js/controllers.min.js' },
            { src: [out + '/taoTestGeneration/controller/routes.js.map'],  dest: root + '/taoTestGeneration/views/js/controllers.min.js.map' }
        ]
    };

    grunt.config('clean', clean);
    grunt.config('requirejs', requirejs);
    grunt.config('copy', copy);

    // bundle task
    grunt.registerTask('taotestgenerationbundle', ['clean:taotestgenerationbundle', 'requirejs:taotestgenerationbundle', 'copy:taotestgenerationbundle']);
};

