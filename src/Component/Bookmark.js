import React, { Component } from 'react';
import { FaRegBookmark, FaBookmark } from "react-icons/fa";


class Bookmark extends Component
{
    constructor(props) {
        super(props);
    }

    render() {
        if(!this.props.isBookmarkClicked)
            return (
                <div>
                    <FaRegBookmark onClick={this.props.clickBookmark}/>
                </div>
            )
        else
            return (
                <div>
                    <FaBookmark />
                </div>
            )
    }
}


export default Bookmark;


