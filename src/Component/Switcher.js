import React, { Component } from "react";
import Switch from "react-switch";


class Switcher extends Component
{
    constructor(props) {
        super(props);
    }


    render() {
        return (
            <Switch
                onChange={this.props.switchApiBase}
                checked={this.props.apiBase}
                onColor={"#08e"}
                offColor={"#dedede"}
                checkedIcon = {false}
                uncheckedIcon = {false}
                activeBoxShadow="0 0 0 0 #3bf"
            />
        );
    }
}

export default Switcher;








