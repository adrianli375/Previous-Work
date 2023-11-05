const blockNos = new Set(["1", "2", "3", "4", "5", "6", "7", "8"]);
const blockIds = ["blk1", "blk2", "blk3", "blk4", "blk5", "blk6", "blk7", "blk8"];
var visibleBlocksNo = ["1", "2", "3", "4", "5", "6", "7", "8"];
var hiddenBlocksNo = [];
const containers = [
    `
        <div id ="1">
            <h2>Block 1 - SP500</h2>
            <p><img src="images/SP500.png" alt="SP500" style="width: 250px;">
            </p>
        </div>
    `, 
    `
        <div id="2">
            <h2>Block 2 - FTSE 100</h2>
            <p><img src="images/FTSE100.png" alt="FTSE 100">
            </p>
        </div>
    `,
    `
        <div id="3">
            <h2>Block 3 - Hang Seng Index</h2>
            <p><img src="images/HSI.png" alt="Hang Seng Index" style="width: 900px;">
            </p>
        </div>
    `,
    `
        <div id="4">
            <h2>Block 4 - Nasdaq Composite index</h2>
            <p><img src="images/nasdaq.png" alt="NASDAQ" style="width: 600px;">
            </p>
        </div>
    `,
    `
        <div id="5">
            <h2>Block 5 - USD Exchange Rate</h2>
            <p><img src="images/ex_rate.png" alt="USD Rate" style="width: 250px;">
            </p>
        </div>
    `,
    `
        <div id="6">
            <h2>Block 6 - Currency Converter</h2>
            <p><img src="images/Convert-Currency.png" alt="Currency Converter" style="width: 300px;">
            </p>
        </div>
    `,
    `
        <div id="7">
            <h2>Block 7 - Crypto Index</h2>
            <p><img src="images/Crypto.png" alt="Crypto Index" style="width: 500px;">
            </p>
        </div>
    `,
    `
        <div id="8">
            <h2>Block 8 - USD vs. HKD</h2>
            <p><img src="images/USD-HKD.png" alt="USD vs. HKD" style="width: 400px;">
            </p>
        </div>
    `
];

// do everything after DOM loaded
$(document).ready(function() {
    // set the ID for the div inside the block
    var visibleBlocks = document.querySelectorAll('#container>div');
    visibleBlocksNo = new Set();
    const regex = /Block (\d)/i;
    for (let block of visibleBlocks) {
        let match = regex.exec(block.innerText)
        if (match) {
            let blockNo = match[1];
            block.setAttribute('id', blockNo);
            visibleBlocksNo.add(blockNo);
        }
    }
    hiddenBlocksNo = new Set([...blockNos].filter(x => !visibleBlocksNo.has(x)));
    // create the toggle button
    var toggleBtn = document.createElement('span');
    toggleBtn.setAttribute('id', 'toggle');
    toggleBtn.innerText = 'Custom';
    document.getElementById('container').prepend(toggleBtn);
    toggleBtn.addEventListener('click', toggle);
});

// drag function
function drag(event) {
    let target = event.target;
    while (!(target instanceof HTMLDivElement)) {
        target = target.parentNode;
    }
    event.dataTransfer.setData('text/plain', target.id);
}

function dragover(event) {
    event.preventDefault();
    let dragBorderStyle = '5px solid pink'
    if (event.target instanceof HTMLDivElement) {
        document.getElementById(event.target.id).style.border = dragBorderStyle;
    }
    else {
        let parent = event.target.parentNode;
        while (!(parent instanceof HTMLDivElement)) {
            parent = parent.parentNode;
        }
        parent.style.border = dragBorderStyle;
    }
}

function dragleave(event) {
    event.preventDefault();
    let originalBorderStyle = '1px dashed red';
    if (event.target instanceof HTMLDivElement) {
        document.getElementById(event.target.id).style.border = originalBorderStyle;
    }
    else {
        let parent = event.target.parentNode;
        while (!(parent instanceof HTMLDivElement)) {
            parent = parent.parentNode;
        }
        parent.style.border = originalBorderStyle;
    }
}

function drop(event) {
    event.preventDefault();
    let blockId = event.dataTransfer.getData('text/plain');
    // restore border settings
    let originalBorderStyle = '1px dashed red';
    let target = event.target;
    if (target instanceof HTMLDivElement) {
        document.getElementById(event.target.id).style.border = originalBorderStyle;
    }
    else {
        while (!(target instanceof HTMLDivElement)) {
            target = target.parentNode;
        }
        target.style.border = originalBorderStyle;
    }
    // update DOM tree
    let draggedBlock = document.getElementById(blockId);
    if (draggedBlock != target) {
        document.getElementById('container').removeChild(draggedBlock);
        target.insertAdjacentElement('afterend', draggedBlock);
    }
}

function preventChildrenDraggable(element) {
    let children = element.children;
    for (let i = 0; i < children.length; i++) {
        let child = children[i];
        child.setAttribute('draggable', 'false');
        preventChildrenDraggable(child);
    }
}

function restoreImgDraggable(element) {
    let children = element.children;
    for (let i = 0; i < children.length; i++) {
        let child = children[i];
        if (child instanceof HTMLImageElement) {
            child.setAttribute('draggable', 'true');
        }
        restoreImgDraggable(child);
    }
}

// create a handler to switch from custom to save
async function toggle() {
    let currentText = document.getElementById('toggle').innerText;
    if (currentText == 'Custom') {
        document.getElementById('toggle').innerText = 'Save';
        // show the visible blocks with the eye open logo
        for (let num of visibleBlocksNo) {
            let block = document.getElementById(num);
            let visibleLogo = '<img class="eye" src="images/eye-open.png"></img>';
            block.insertAdjacentHTML('afterbegin', visibleLogo);
        }
        // show the hidden blocks in custom mode
        for (let num of hiddenBlocksNo) {
            let blockHTML = containers[num-1];
            document.getElementById('container').insertAdjacentHTML('beforeend', blockHTML);
            let block = document.getElementById(num);
            let hiddenLogo = '<img class="eye" src="images/eye-close.png"><img>';
            block.insertAdjacentHTML('afterbegin', hiddenLogo);
        }
        // set all blocks to have border specified
        var allBlocks = document.querySelectorAll('#container>div');
        for (let i = 0; i < Object.keys(allBlocks).length; i++) {
            let block = allBlocks[i];
            block.style.border = '1px dashed red';
            // add the event listener to the eye img
            let eyeImg = block.querySelector('.eye');
            eyeImg.addEventListener('click', function() {
                let currentImg = document.getElementById(block.getAttribute('id')).querySelector('.eye')
                let currentImgSrc = currentImg.getAttribute('src');
                let currentBlock = allBlocks[i];
                if (currentImgSrc == 'images/eye-open.png') {
                    currentImg.setAttribute('src', 'images/eye-close.png');
                    document.getElementById('container').removeChild(currentBlock);
                    document.getElementById('container').appendChild(currentBlock);
                }
                else if (currentImgSrc == 'images/eye-close.png') {
                    currentImg.setAttribute('src', 'images/eye-open.png');
                    document.getElementById('container').removeChild(currentBlock);
                    document.getElementById('container').insertBefore(
                        currentBlock, document.getElementById('container').children[1]
                    );
                }
            });
            // enable the drag and drop properties
            block.setAttribute('draggable', 'true');
            preventChildrenDraggable(block);
            block.setAttribute('ondragstart', 'drag(event)');
            block.setAttribute('ondragover', 'dragover(event)');
            block.setAttribute('ondragenter', 'dragover(event)');
            block.setAttribute('ondragleave', 'dragleave(event)');
            block.setAttribute('ondrop', 'drop(event)');
        }
    }
    else if (currentText == 'Save') {
        document.getElementById('toggle').innerText = 'Custom';
        // remove border settings
        var allBlocks = document.querySelectorAll('#container>div');
        for (let i = 0; i < Object.keys(allBlocks).length; i++) {
            let block = allBlocks[i];
            // remove the event listener to the eye img
            let eyeImg = block.querySelector('.eye');
            let divId = block.getAttribute('id');
            eyeImg.removeEventListener('click', function() {
                let currentImg = document.getElementById(block.getAttribute('id')).querySelector('.eye')
                let currentImgSrc = currentImg.getAttribute('src');
                let currentBlock = allBlocks[i];
                if (currentImgSrc == 'images/eye-open.png') {
                    currentImg.setAttribute('src', 'images/eye-close.png');
                    document.getElementById('container').removeChild(currentBlock);
                    document.getElementById('container').appendChild(currentBlock);
                }
                else if (currentImgSrc == 'images/eye-close.png') {
                    currentImg.setAttribute('src', 'images/eye-open.png');
                    document.getElementById('container').removeChild(currentBlock);
                    document.getElementById('container').insertBefore(
                        currentBlock, document.getElementById('container').children[1]
                    );
                }
            });
            // get the state of the element, visible or hidden?
            let blockVisible = eyeImg.getAttribute('src') == 'images/eye-open.png' ? true : false;
            if (!blockVisible) {
                visibleBlocksNo.delete(divId);
                hiddenBlocksNo.add(divId);
            }
            else {
                if (hiddenBlocksNo.has(divId)) {
                    hiddenBlocksNo.delete(divId);
                    visibleBlocksNo.add(divId);
                }
            }
            // disable the drag and drop properties
            restoreImgDraggable(block);
            let properties = ['draggable', 'ondragstart', 'ondragover', 'ondragenter', 'ondragleave', 'ondrop'];
            for (let attr of properties) {
                block.removeAttribute(attr);
            }
            block.style.border = 'none';
        }
        // make the eye logo invisible
        $('.eye').css({'display': 'none'});
        // remove the hidden blocks
        for (let num of hiddenBlocksNo) {
            document.getElementById(num).remove();
        }
        // save user preferences
        let visibleBlockIds = [];
        let hiddenBlockIds = [];
        var visibleBlocks = document.querySelectorAll('#container>div');
        for (let block of visibleBlocks) {
            let blockId = `blk${block.id}`;
            visibleBlockIds.push(blockId);
        }
        for (let blockId of blockIds) {
            if (!(visibleBlockIds.includes(blockId))) {
                hiddenBlockIds.push(blockId);
            }
        }
        var visible = '"' + visibleBlockIds.join('", "') + '"';
        console.log(visible);
        document.cookie = `visible=${visible}`;
        var hidden = '"' + hiddenBlockIds.join('", "') + '"';
        console.log(hidden);
        document.cookie = `hidden=${hidden}`;
        // finally, send the put request to the server
        await fetch('index.php', {
            method: 'PUT', 
            credentials: 'same-origin'
        })
        .then(response => {
            console.log(response.status, response.statusText);
            if (response.status == 200) {
                response.text().then(data => {
                    console.log(data);
                    document.cookie = `uid=${data}`;
                });
            }
            else if (response.status == 400) {
                console.log('The request sent does not carry either user ID or preferences!');
            }
            else if (response.status == 500) {
                console.log('Cannot connect to the database');
            }
        })
        .catch(() => {
            console.log('Cannot send the put request!');
        });
        // remove cookies
        document.cookie = "visible=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
        document.cookie = "hidden=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
    }
}