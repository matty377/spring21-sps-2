// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

const colors = 
['mint', 'ivory', 'grey', 'fuchsia', 'pink', 'indigo',
'lime', 'orchid', 'purple', 'yellow', 'black', 'white',
'turquoise', 'silver', 'sky', 'plum', 'red',
'lavender', 'green', 'teal'];
var tagSet = new Set();
var filtered = false;

/**
 * Creates an image
 * @param {URL} src 
 * @param {Element} parent not used for now, maybe needed later.
 * @returns {Element} img
 */
function createImage(src, parent) {
    let img = document.createElement("img");
    img.src = src;
    return img;
}

/**
 * Appends paragraph text to given parent
 * @param {String} txt 
 * @param {Element} parent 
 */
function appendText(txt, parent) {
    let p = document.createElement("p");
    p.innerText = txt;
    parent.appendChild(p);
}
/**
 * Returns a list of random tags (This is temporary)
 */
function returnRandomTags() {
    let indicies = [];
    let out = [];
    while (indicies.length < 6) {    //Iterate through to add random tags
        let currIndex = Math.floor(Math.random() * colors.length);
        if (indicies.indexOf(currIndex) < 0) {
            out.push(colors[currIndex]);
            indicies.push(currIndex);
            tagSet.add(colors[currIndex]);
        }
    }
    return out;
}

/**
 * Creates buttons given the values of the tagSet.
 */
function createButtons() {
    for (let tagName of tagSet.values()) {
        let buttonDiv = document.getElementById("buttons");
        buttonDiv.appendChild(createButton(tagName));
    }
}

/**
 * Creates button using tag.
 * @param {String} tag tag of button
 */
function createButton(tag) {
    let button = document.createElement('button');
    button.innerHTML = tag;
    let imgList = document.getElementById("imgList");

    button.addEventListener('click', function(){
        if (filtered === false) {
            //This is just setting visiblity
            Array.from(imgList.children).forEach(element => setVisibility(element, true));
            Array.from(document.getElementsByClassName(tag)).forEach(element => setVisibility(element, false));
            filtered = true;

        } else {
            Array.from(imgList.children).forEach(element => setVisibility(element, false));
            filtered = false;
        }
    });

    return button;
}

/**
 * Sets visiblity of an element
 * @param {Element} element Element to make hidden/visible
 * @param {Boolean} hide Whether to hide element or not
 */
var setVisibility = function(element, hide) {
    if (hide) {
        element.style.visibility = "hidden";
    } else {
        element.style.visibility = "visible";        
    }
}

/**
 * Fetches images and creates buttons, adding them to the webpage.
 */
async function loadElements() {
    const responseFromServer = await fetch('/images');
    const jsonFromServ = await responseFromServer.json();
    const imgList = document.getElementById("imgList");
    console.log(jsonFromServ);
    const doc = document;
    if (jsonFromServ.length > 0) {
        jsonFromServ.forEach(function(src) {
            let parent = document.createElement("li");
            let img = createImage(src.Url);
            let tags = returnRandomTags(); //this will be the tags
            tags.forEach(element => parent.classList.add(element)); //classes to de-select
            parent.appendChild(img);
            appendText(src.message, parent);
            imgList.appendChild(parent);
        });
    }
    createButtons();
}