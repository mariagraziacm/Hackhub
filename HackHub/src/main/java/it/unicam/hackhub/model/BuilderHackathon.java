package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.model.Hackathon;

public class BuilderHackathon {
        private String id;
        private String name;
        private String specifications;

        public BuilderHackathon setId(String id) {
            this.id = id;
            return this;
        }

        public BuilderHackathon setName(String name) {
            this.name = name;
            return this;
        }

        public BuilderHackathon setSpecifications(String specifications) {
            this.specifications = specifications;
            return this;
        }

        public Hackathon build() {

            if (id == null || name == null) {
                throw new IllegalStateException("Id e Name obbligatori");
            }

            return new Hackathon(id, name, specifications);
        }
}