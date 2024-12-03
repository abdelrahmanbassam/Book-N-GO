import {colors} from "./styles";
import PropTypes from "prop-types";

export const Rate = (props) => {
    return (
        <>
            <div className="flex gap-1 items-center">
                {[...Array(5)].map((_, i) => (
                    <img
                        key={i} className={(i < props.stars ? "" : "grayscale") + " w-5 h-5 f"}
                        src={"assets/star.png"} alt=""/>
                ))}
                <span
                    style={{color: colors.text}}
                    className="text-black text-lg ml-2">({props.rate})
                </span>
            </div>
        </>
    )
}

Rate.propTypes = {
    rate: PropTypes.number,
    stars: PropTypes.number,
};