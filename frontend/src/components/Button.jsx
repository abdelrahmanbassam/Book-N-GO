import PropTypes from "prop-types";
import {colors} from "./styles";

export const Button = (props) => {
    return (
        <>
            <button
                style={{
                    backgroundColor: props.BackgroundColor || colors.secondary2,
                    color: props.color || colors.primary,
                    width: props.width || "100px",
                    height: props.height || "40px",
                }}
                onMouseOver={
                    (e) => {
                        e.target.style.backgroundColor = props.hoverColor || colors.secondary2Hover;
                        e.target.style.color = colors.text;
                    }
                }
                onMouseOut={
                    (e) => {
                        e.target.style.backgroundColor = props.BackgroundColor || colors.secondary2;
                        e.target.style.color = props.color || colors.primary;
                    }
                }
                className={"px-4 py-2 tr " + props.className }
                onClick={props.onClick}
            >
                {props.children}
            </button>
        </>
    )
}

Button.propTypes = {
    className: PropTypes.string,
    onClick: PropTypes.func,
    width: PropTypes.string,
    height: PropTypes.string,
    backgroundColor: PropTypes.string,
    color: PropTypes.string,
    hoverColor: PropTypes.string,
    children: PropTypes.node,
};