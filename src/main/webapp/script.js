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

var tagSet = new Set();
var filtered = false;

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
 * Takes an array of tags and adds to a set
 * @param {Array} tagsToAdd 
 */

function putTagsIntoSet(tagsToAdd) { //Honestly this might be better to do on backend maybe, cause this is doing it each time, but MVP
    tagsToAdd.forEach(tag => tagSet.add());
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
        element.style.display = "none";
    } else {
        element.style.display = "block";        
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
        imgList.removeChild(imgList.children[0]); // Remove our helpful loading cat. Meow.        
        jsonFromServ.forEach(function(src) {
            let parent = document.createElement("li");
            let img = createImage(src.Url);
            let tags = src.tags; //this will be the tags
            tags.forEach(element => tagSet.add(element.value.replaceAll(" ","-")));            
            tags.forEach(element => parent.classList.add(element.value.replaceAll(" ", "-"))); //classes to de-select
            img.setAttribute("onClick", "openComments(this)")
            //tags.forEach(element => img.classList.add(element.value.replaceAll(" ", "-"))); //Add tags to image to retrieve later Actually may not be needed.            
            parent.appendChild(img);
            appendText(src.message, parent);
            imgList.appendChild(parent);
        });
    }
    //TODO: make it so it only creates n number of buttons and then an option to show all or random maybe? will handle later
    createButtons();
}

/**
 * Creates link given image element
 * @param {Element} img image element
 */
function createLink(img) {
    const destUrl = "comments.html";
    let params = new URLSearchParams();
    params.append('imageUrl', encodeURIComponent(img.src));
    return (destUrl + "?" + params.toString());
}

/**
 * Opens a window given the image
 * @param {Element} img 
 */
function openComments(img) {
    window.location.href = createLink(img);
}

/**
 * Decodes a link from a query
 * @param {String} query query string
 */
function decodeLink(query) {
    let params = new URLSearchParams(query);
    let imgUrl = params.get('imageUrl');
    return (decodeURIComponent(imgUrl));
}

/**
 * Displays comments on load
 */
async function displayComments() {
    let src = decodeLink(window.location.search.slice(1));
    let imgContainer = document.getElementById("img-container");

    imgContainer.appendChild(createImage(src));  

    let imgData = {sentText : src};
    const sendLink = await sendUrl('/comment', imgData);

    let dataOut = await getData();
    let message = dataOut.message;
    let comments = dataOut.replies; //Should be an array

    let parent = document.createElement("li");
    let messageList = document.getElementById("commentList");
    let replyList = document.getElementById("replyList");

    appendText(message, parent);    
    messageList.appendChild(parent);

    if (comments[0] != null) {
        comments.forEach(msg => {
            let listElem = document.createElement("li");
            appendText(msg.value, listElem);
            replyList.appendChild(listElem);
        });
    } else {
        let listElem = document.createElement("li");
        appendText("No replies :(", listElem);
        replyList.appendChild(listElem);
    }
    
}

/**
 * 
 * @param {url} url url to rend request
 * @param {Object} data data for body
 */
async function sendUrl(url, data) {
    let response = fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    return response;    
}

/**
 * gets Data from server
 * @param {String} url url to fetch 
 */
async function getData(url) {
    const responseFromServer = await fetch('/comment');
    const jsonFromServ = await responseFromServer.json();
    console.log(jsonFromServ);
    return jsonFromServ[0];
}
/**
 * Sends a comment to the server
 */
async function sendMessage() {
    let imgSrc = decodeLink(window.location.search.slice(1)); //Image source, used to find datastore object
    let message = document.getElementById("reply").value;
    let messageData = {sentUrl : imgSrc, sentMsg : message}
    const sendStuff = await sendUrl('/post-comment', messageData);    
}