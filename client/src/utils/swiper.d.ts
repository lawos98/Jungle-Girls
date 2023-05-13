import React from "react";

import type { SwiperSlideProps, SwiperProps } from "swiper/react";

declare global {
    namespace JSX {
        interface IntrinsicElements {
            "swiper-container": React.DetailedHTMLProps<
                React.HTMLAttributes<HTMLElement> & SwiperProps,
                HTMLElement
            >;
            "swiper-slide": React.DetailedHTMLProps<
                React.HTMLAttributes<HTMLElement> & SwiperSlideProps,
                HTMLElement
            >;
        }
    }
}
{/*<swiper-container navigation={true} slides-per-view="3"*/}
{/*                  space-between="30" centered-slides="true" grab-cursor="true"*/}
{/*                  ref={swiperRef}*/}
{/*>*/}
{/*    {exampleRowLabels.map((label, index) => (*/}
{/*        <swiper-slide key={index}>{label}</swiper-slide>*/}
{/*    ))}*/}
{/*</swiper-container>*/}