package at.hugo.papermc.plugin.example.plugin;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class ExamplePluginClassLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("at.hugob.plugin.library:gui:0.0.2"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("at.hugob.plugin.library:config:1.1.2"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("at.hugob.plugin.library:database:1.0.0"), null));

        resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build());
        classpathBuilder.addLibrary(resolver);
    }
}
